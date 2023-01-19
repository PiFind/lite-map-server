package io.pifind.map3rd.amap.service.impl;

import io.pifind.common.response.R;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.map3rd.api.IGeocodingService;
import io.pifind.map3rd.model.GeocodingDTO;
import io.pifind.map3rd.model.ReverseGeocodingDTO;
import org.springframework.stereotype.Service;

import static io.pifind.map3rd.amap.AmapConstants.PLATFORM_NAME;

/**
 * 统一地理编码服务实现
 * @see IGeocodingService
 */
@Service(PLATFORM_NAME + "-GeocodingService")
public class GeocodingServiceImpl implements IGeocodingService {

    @Override
    public R<GeocodingDTO> geocoding(String address, String language) {
        return null;
    }

    @Override
    public R<ReverseGeocodingDTO> reverseGeocoding(CoordinateDTO coordinate, String language) {
        return null;
    }

}
