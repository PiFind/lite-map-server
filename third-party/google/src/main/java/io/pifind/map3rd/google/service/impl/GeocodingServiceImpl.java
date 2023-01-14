package io.pifind.map3rd.google.service.impl;

import io.pifind.common.response.R;
import io.pifind.common.response.StandardCode;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.map3rd.api.IGeocodingService;
import io.pifind.map3rd.google.converter.CoordinateDtoConverter;
import io.pifind.map3rd.google.model.dto.GoogleCoordinateDTO;
import io.pifind.map3rd.google.model.dto.GoogleGeocodingDTO;
import io.pifind.map3rd.google.model.qo.GeocodingQO;
import io.pifind.map3rd.google.service.IGoogleGeocodingService;
import io.pifind.map3rd.model.GeocodingDTO;
import io.pifind.map3rd.model.ReverseGeocodingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 统一地理编码服务实现
 */
@Service("Google-GeocodingService")
public class GeocodingServiceImpl implements IGeocodingService {

    @Autowired
    private IGoogleGeocodingService googleGeocodingService;

    @Autowired
    private CoordinateDtoConverter coordinateDtoConverter;

    @Override
    public R<GeocodingDTO> geocoding(String address) {
        GeocodingQO qo = new GeocodingQO();

        R<List<GoogleGeocodingDTO>> result = googleGeocodingService.geocoding(qo);
        if (result.getCode() != StandardCode.SUCCESS) {
            return new R<>(
                    result.getCode(),
                    result.getMessage()
            );
        }

        List<GoogleGeocodingDTO> googleGeocodingData = result.getData();
        if (googleGeocodingData.isEmpty()) {
            return R.failure();
        } else {

            // 只获取第一个
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
    }

    @Override
    public R<ReverseGeocodingDTO> reverseGeocoding(CoordinateDTO coordinate) {




        return null;
    }

}
