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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

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

        // 创建一个反向编码实体
        ReverseGeocodingDTO reverseGeocodingDTO = new ReverseGeocodingDTO();
        reverseGeocodingDTO.setFullName(googleGeocoding.getFormattedAddress());
        for (GoogleGeocodingDTO.AddressComponent addressComponent : googleGeocoding.getAddressComponents()) {
            // TODO 进行注入



        }
        return R.success(reverseGeocodingDTO);
    }

}
