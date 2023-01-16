package io.pifind.map3rd.google.service.impl;

import io.pifind.common.response.R;
import io.pifind.common.response.StandardCode;
import io.pifind.map.constant.GeographicCoordinateSystemEnum;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.map3rd.api.IGeocodingService;
import io.pifind.map3rd.error.MapApiCode;
import io.pifind.map3rd.error.ThirdPartMapServiceException;
import io.pifind.map3rd.google.converter.CoordinateDtoConverter;
import io.pifind.map3rd.google.model.dto.GoogleCoordinateDTO;
import io.pifind.map3rd.google.model.dto.GoogleGeocodingDTO;
import io.pifind.map3rd.google.model.qo.GeocodingQO;
import io.pifind.map3rd.google.model.qo.ReverseGeocodingQO;
import io.pifind.map3rd.google.service.IGoogleGeocodingService;
import io.pifind.map3rd.model.GeocodingDTO;
import io.pifind.map3rd.model.ReverseGeocodingDTO;
import io.pifind.map3rd.model.SingleDistrictDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static io.pifind.map3rd.google.GoogleConstants.PLATFORM_NAME;

/**
 * 统一地理编码服务实现
 * @see IGeocodingService
 */
@Service(PLATFORM_NAME + "-GeocodingService")
public class GeocodingServiceImpl implements IGeocodingService {

    @Autowired
    private IGoogleGeocodingService googleGeocodingService;

    @Autowired
    private CoordinateDtoConverter coordinateDtoConverter;

    @Override
    public R<GeocodingDTO> geocoding(String address,String language) {

        // 创建一个查询对象
        GeocodingQO qo = new GeocodingQO();
        qo.setAddress(address);
        if (language != null) {
            qo.setLanguage(language);
        } else {
            qo.setLanguage(LocaleContextHolder.getLocale().getLanguage());
        }

        // 获取查询结果
        R<List<GoogleGeocodingDTO>> result = googleGeocodingService.geocoding(qo);
        if (result.getCode() != StandardCode.SUCCESS) {
            return R.failure(result.getCode(),result.getMessage());
        }

        // 只获取第一个
        List<GoogleGeocodingDTO> googleGeocodingData = result.getData();
        GoogleGeocodingDTO googleGeocoding = googleGeocodingData.get(0);

        // 创建一个地理编码实体
        GeocodingDTO geocodingDTO = new GeocodingDTO();
        geocodingDTO.setName(googleGeocoding.getFormattedAddress());

        // 获取坐标
        GoogleCoordinateDTO googleCoordinateDTO = googleGeocoding.getGeometry().getLocation();
        if (googleCoordinateDTO != null) {
            CoordinateDTO coordinateDTO = coordinateDtoConverter.convert(googleCoordinateDTO);
            geocodingDTO.setCoordinate(coordinateDTO);
        }

        return R.success(geocodingDTO);
    }

    @Override
    public R<ReverseGeocodingDTO> reverseGeocoding(CoordinateDTO coordinate,String language) {

        // 检查是否是 Google API 支持的地理坐标系
        if (! coordinate.getSystem().equals(GeographicCoordinateSystemEnum.WGS84)) {
            throw new ThirdPartMapServiceException(
                    MapApiCode.UNSUPPORTED_SERVICE_ERROR,
                    "Google API 不支持 " + coordinate.getSystem() + " 地理坐标系反向地理编码查询"
            );
        }

        // 创建查询对象
        ReverseGeocodingQO qo = new ReverseGeocodingQO();
        qo.setLatlng(String.format("%f,%f",coordinate.getLatitude(),coordinate.getLongitude()));
        qo.setLanguage(language);

        // 进行查询
        R<List<GoogleGeocodingDTO>> result = googleGeocodingService.reverseGeocoding(qo);
        if (result.getCode() != StandardCode.SUCCESS) {
            return R.failure(result.getCode(),result.getMessage());
        }

        // 只获取第一个
        List<GoogleGeocodingDTO> googleGeocodingData = result.getData();
        GoogleGeocodingDTO googleGeocoding = googleGeocodingData.get(0);
        System.out.println(googleGeocoding);

        // 创建一个反向编码实体
        ReverseGeocodingDTO reverseGeocodingDTO = new ReverseGeocodingDTO();
        reverseGeocodingDTO.setFullName(googleGeocoding.getFormattedAddress());

        /*
         * 下面是收集返回的结果的过程
         */

        SingleDistrictDTO country = null;
        List<SingleDistrictDTO> administrativeAreas = new ArrayList<>();
        SingleDistrictDTO locality = null;
        List<SingleDistrictDTO> subLocalities = new ArrayList<>();

        for (GoogleGeocodingDTO.AddressComponent addressComponent : googleGeocoding.getAddressComponents()) {

            SingleDistrictDTO district = new SingleDistrictDTO();

            // 取地址组件全名为区域名
            district.setName(addressComponent.getLongName());

            // 根据返回的类型判断是国家还是行政区，并设置等级
            List<String> types = addressComponent.getTypes();

            // 查找行政区
            for (String type : types) {
                String typeLowerCase = type.toLowerCase();
                if (typeLowerCase.matches("^administrative_area_level_[1-7]$")) {
                    // 行政区
                    district.setLevel(Integer.valueOf(typeLowerCase.substring(26)));
                    administrativeAreas.add(district);
                    break;
                } else if (typeLowerCase.matches("^sublocality_level_[1-5]$")) {
                    // 市级行政区下的区划
                    district.setLevel(Integer.valueOf(typeLowerCase.substring(18)));
                    subLocalities.add(district);
                    break;
                } else if (typeLowerCase.equals("country")) {
                    // 国家
                    country = district;
                    district.setLevel(0);
                    // 如果是国家的话，那么其简称就是 ISO 3166-1 规定的国家代码
                    reverseGeocodingDTO.setCountryCode(addressComponent.getShortName());
                    break;
                } else if (typeLowerCase.equals("locality")) {
                    // 市级行政区
                    locality = district;
                    break;
                }
            }
        }

        /*
         * 下面是对上面收集的结果进行标准化的过程
         */

        List<SingleDistrictDTO> districts = new ArrayList<>();
        if (country != null) {

            // 添加国家
            districts.add(country);

            // 添加国家下的行政区
            districts.addAll(administrativeAreas);

            if (locality != null) {
                // 计算市级行政区开始等级
                int startLevel = administrativeAreas.size() + 1;
                // 添加市级行政区
                locality.setLevel(startLevel);
                districts.add(locality);
                // 更新市下面的区的等级
                for (SingleDistrictDTO subLocality : subLocalities) {
                    subLocality.setLevel(startLevel + subLocality.getLevel());
                }
                // 添加市级行政区下的区划
                districts.addAll(subLocalities);
            }
        }

        // 根据等级进行排序
        districts.sort(ReverseGeocodingDTO.DISTRICT_COMPARATOR);
        reverseGeocodingDTO.setDistricts(districts);

        return R.success(reverseGeocodingDTO);
    }

}
