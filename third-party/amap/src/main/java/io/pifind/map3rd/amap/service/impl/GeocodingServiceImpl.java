package io.pifind.map3rd.amap.service.impl;

import io.pifind.common.response.R;
import io.pifind.common.response.StandardCode;
import io.pifind.map.constant.GeographicCoordinateSystemEnum;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.map.util.GeoCoordinateUtils;
import io.pifind.map3rd.amap.converter.CoordinateDtoConverter;
import io.pifind.map3rd.amap.model.dto.AmapGeocodingDTO;
import io.pifind.map3rd.amap.model.dto.AmapReverseGeocodingDTO;
import io.pifind.map3rd.amap.model.dto.component.AmapAddressDTO;
import io.pifind.map3rd.amap.model.qo.GeocodingQO;
import io.pifind.map3rd.amap.model.qo.ReverseGeocodingQO;
import io.pifind.map3rd.amap.service.IAmapGeocodingService;
import io.pifind.map3rd.api.IGeocodingService;
import io.pifind.map3rd.error.MapApiCode;
import io.pifind.map3rd.error.ThirdPartMapServiceException;
import io.pifind.map3rd.model.GeocodingDTO;
import io.pifind.map3rd.model.component.GeographicalTargetDTO;
import io.pifind.map3rd.model.ReverseGeocodingDTO;
import io.pifind.map3rd.model.component.SingleDistrictDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static io.pifind.map3rd.amap.AmapConstants.PLATFORM_NAME;

/**
 * 统一地理编码服务实现
 * @see IGeocodingService
 */
@Service(PLATFORM_NAME + "-GeocodingService")
public class GeocodingServiceImpl implements IGeocodingService {

    @Autowired
    private IAmapGeocodingService amapGeocodingService;

    @Autowired
    private CoordinateDtoConverter coordinateDtoConverter;

    /**
     * 地理编码
     * @param address 地址
     * @param language 使用的语言，如果为 {@code null}，那么将自动选择本地语言
     * @return 地理编码查询结果
     */
    @Override
    public R<GeocodingDTO> geocoding(String address, String language) {

        // 如果语言为空，那么自动选择本地语言
        if (language == null) {
            language = LocaleContextHolder.getLocale().getLanguage();
        }

        // 因为我们只适配了高德地图的国内服务，不支持中文以外语言的返回
        if (!(language.equals(Locale.CHINA.getLanguage()))) {
            throw new ThirdPartMapServiceException(MapApiCode.UNSUPPORTED_SERVICE_ERROR,"高德地图的国内服务不支持中文以外语言的返回");
        }

        // 查询结果
        GeocodingQO qo = new GeocodingQO(address);
        R<AmapGeocodingDTO> result =  amapGeocodingService.geocoding(qo);
        if (result.getCode() != StandardCode.SUCCESS) {
            return R.failure(result.getCode(),result.getMessage());
        }

        // 提取坐标数据
        AmapGeocodingDTO amapGeocoding = result.getData();
        List<GeographicalTargetDTO> targetList = new ArrayList<>();
        for (AmapGeocodingDTO.Geocode geocode : amapGeocoding.getGeocodes()) {

            GeographicalTargetDTO geographicalTargetDto = new GeographicalTargetDTO();

            // 创建一个坐标数据
            CoordinateDTO coordinate =
                    coordinateDtoConverter.convert(geocode.getLocation());
            geographicalTargetDto.setCoordinate(coordinate);

            // 然后获取地址名
            StringBuilder fullNameBuilder = new StringBuilder();
            String[] administrativeAreaNames = new String[] {
                    geocode.getCountry(),
                    geocode.getProvince(), // 省份
                    geocode.getCity(),     // 城市
                    geocode.getDistrict(), // 区
                    geocode.getStreet(),   // 街道
            };

            for (String administrativeAreaName : administrativeAreaNames) {
                if (administrativeAreaName != null) {
                    fullNameBuilder.append(administrativeAreaName);
                }
            }

            geographicalTargetDto.setName(fullNameBuilder.toString());

            // 添加结果
            targetList.add(geographicalTargetDto);
        }

        GeocodingDTO geocoding = new GeocodingDTO();
        geocoding.setCount(targetList.size());
        geocoding.setTargets(targetList);

        return R.success(geocoding);
    }

    /**
     * 逆地理编码
     * @param coordinate 坐标
     * @param language 使用的语言，如果为 {@code null}，那么将自动选择本地语言
     * @return 逆地理编码查询结果
     */
    @Override
    public R<ReverseGeocodingDTO> reverseGeocoding(CoordinateDTO coordinate, String language) {

        // 如果语言为空，那么自动选择本地语言
        if (language == null) {
            language = LocaleContextHolder.getLocale().getLanguage();
        }

        // 因为我们只适配了高德地图的国内服务，不支持中文以外语言的返回
        if (!(language == null || language.equals(Locale.CHINA.getLanguage()))) {
            throw new ThirdPartMapServiceException(MapApiCode.UNSUPPORTED_SERVICE_ERROR,"高德地图的国内服务不支持中文以外语言的返回");
        }

        // 检查坐标系是否为 GCJ02 ，因为高德地图只支持 GCJ02 坐标系
        CoordinateDTO gcj02Coordinate;
        if (! coordinate.getSystem().equals(GeographicCoordinateSystemEnum.GCJ02)) {
            // 如果不是 GCJ02 ， 检查是否为 WGS84 坐标系，如果是那么就将经纬度转换为 GCJ02 坐标系
            if (coordinate.getSystem().equals(GeographicCoordinateSystemEnum.WGS84)) {
                double[] newPositions =
                        GeoCoordinateUtils.WGS84ToGCJ02(coordinate.getLatitude(), coordinate.getLongitude());
                // 创建新的坐标
                gcj02Coordinate = new CoordinateDTO(newPositions[1],newPositions[0]);
                gcj02Coordinate.setSystem(GeographicCoordinateSystemEnum.GCJ02);
            } else {
                throw new ThirdPartMapServiceException(MapApiCode.UNSUPPORTED_SERVICE_ERROR,
                        "高德地图的国内服务不支持 " + coordinate.getSystem() + " 坐标系");
            }
        } else {
            gcj02Coordinate = coordinate;
        }

        /*
         * 获取从高德地图开放平台的反向地理编码返回对象
         */

        // 构建查询对象并进行查询
        ReverseGeocodingQO qo = new ReverseGeocodingQO(gcj02Coordinate.getLongitude(),gcj02Coordinate.getLatitude());
        R<AmapReverseGeocodingDTO> result =  amapGeocodingService.reverseGeocoding(qo);

        if (result.getCode() != StandardCode.SUCCESS) {
            return R.failure(result.getCode(),result.getMessage());
        }

        AmapReverseGeocodingDTO amapReverseGeocoding =  result.getData();
        AmapReverseGeocodingDTO.ReverseGeocode reverseGeocode = amapReverseGeocoding.getReverseGeocode();

        /*
         * 构建标准反向地理编码对象
         */

        ReverseGeocodingDTO reverseGeocoding = new ReverseGeocodingDTO();
        reverseGeocoding.setFullName(reverseGeocode.getFormattedAddress());
        reverseGeocoding.setCountryCode("CN");

        // 构建行政区划
        List<SingleDistrictDTO> districts = new ArrayList<>();
        AmapAddressDTO amapAddress = reverseGeocode.getAddressComponent();

        String[] administrativeAreaNames = new String[] {
                "中国",
                amapAddress.getProvince(), // 省份
                amapAddress.getCity(), // 城市
                amapAddress.getDistrict(), // 区
                amapAddress.getStreetNumber().getStreet(), // 街道
        };

        // 添加行政区
        int level = 0;
        for (String administrativeAreaName : administrativeAreaNames) {
            if (administrativeAreaName != null) {
                SingleDistrictDTO areaDto = new SingleDistrictDTO();
                areaDto.setLevel(level);
                areaDto.setName(administrativeAreaName);
                districts.add(areaDto);
                level ++;
            }
        }

        reverseGeocoding.setDistricts(districts);
        return R.success(reverseGeocoding);
    }

}
