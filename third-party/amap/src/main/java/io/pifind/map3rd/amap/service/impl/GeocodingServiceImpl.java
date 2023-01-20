package io.pifind.map3rd.amap.service.impl;

import io.pifind.common.response.R;
import io.pifind.common.response.StandardCode;
import io.pifind.map.constant.GeographicCoordinateSystemEnum;
import io.pifind.map.model.CoordinateDTO;
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
            StringBuilder fullNameBuilder = new StringBuilder(geocode.getCountry());

            // 省份
            if (geocode.getProvince() != null) {
                fullNameBuilder.append(geocode.getProvince());
            }
            // 城市
            if (geocode.getCity() != null) {
                fullNameBuilder.append(geocode.getCity());
            }
            // 区
            if (geocode.getDistrict() != null) {
                fullNameBuilder.append(geocode.getDistrict());
            }
            // 街道
            if (geocode.getStreet() != null) {
                fullNameBuilder.append(geocode.getStreet());
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

        if (! coordinate.getSystem().equals(GeographicCoordinateSystemEnum.GCJ02)) {
            throw new ThirdPartMapServiceException(MapApiCode.UNSUPPORTED_SERVICE_ERROR,
                    "高德地图的国内服务不支持 " + coordinate.getSystem() + " 坐标系");
        }

        ReverseGeocodingQO qo = new ReverseGeocodingQO(coordinate.getLongitude(),coordinate.getLatitude());
        R<AmapReverseGeocodingDTO> result =  amapGeocodingService.reverseGeocoding(qo);

        if (result.getCode() != StandardCode.SUCCESS) {
            return R.failure(result.getCode(),result.getMessage());
        }

        AmapReverseGeocodingDTO amapReverseGeocoding =  result.getData();
        AmapReverseGeocodingDTO.ReverseGeocode reverseGeocode = amapReverseGeocoding.getReverseGeocodes().get(0);

        // 构建
        ReverseGeocodingDTO reverseGeocoding = new ReverseGeocodingDTO();
        reverseGeocoding.setFullName(reverseGeocode.getFormattedAddress());
        reverseGeocoding.setCountryCode("CN");

        // 构建行政区划
        List<SingleDistrictDTO> districts = new ArrayList<>();
        AmapAddressDTO amapAddress = reverseGeocode.getAddressComponent();

        int level = 0;
        // 国家
        SingleDistrictDTO countryDto = new SingleDistrictDTO();
        countryDto.setName("中国");
        countryDto.setLevel(level);
        districts.add(countryDto);
        level ++;

        // 省份
        if (amapAddress.getProvince() != null) {
            SingleDistrictDTO provinceDto = new SingleDistrictDTO();
            provinceDto.setLevel(level);
            provinceDto.setName(amapAddress.getProvince());
            districts.add(provinceDto);
            level ++;
        }

        // 城市
        if (amapAddress.getCity() != null) {
            SingleDistrictDTO cityDto = new SingleDistrictDTO();
            cityDto.setLevel(level);
            cityDto.setName(amapAddress.getCity());
            districts.add(cityDto);
            level++;
        }

        // 区
        if (amapAddress.getDistrict() != null) {
            SingleDistrictDTO districtDto = new SingleDistrictDTO();
            districtDto.setLevel(level);
            districtDto.setName(amapAddress.getDistrict());
            districts.add(districtDto);
            level++;
        }

        // 街道
        if (amapAddress.getStreetNumber() != null) {
            SingleDistrictDTO streetDto = new SingleDistrictDTO();
            streetDto.setLevel(level);
            streetDto.setName(amapAddress.getStreetNumber().getStreet());
            districts.add(streetDto);
            level++;
        }

        reverseGeocoding.setDistricts(districts);

        return R.success(reverseGeocoding);
    }

}
