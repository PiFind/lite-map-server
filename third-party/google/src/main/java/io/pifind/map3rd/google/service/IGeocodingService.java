package io.pifind.map3rd.google.service;

import io.pifind.common.response.R;
import io.pifind.map3rd.google.model.dto.GeocodingDTO;
import io.pifind.map3rd.google.model.qo.GeocodingQO;
import io.pifind.map3rd.google.model.qo.ReverseGeocodingQO;

import java.util.List;

/**
 * 地理编码服务
 */
public interface IGeocodingService {

    /**
     * 地理编码，参考：
     * <a href="https://developers.google.com/maps/documentation/geocoding/requests-geocoding">Google API 地理编码</a>
     * @param qo {@link GeocodingDTO 地理编码实体对象}
     * @return 地理编码查询结果
     */
    R<List<GeocodingDTO>> geocoding(GeocodingQO qo);

    /**
     * 反向地理编码，参考：
     * <a href="https://developers.google.com/maps/documentation/geocoding/requests-reverse-geocoding">Google API 反向地理编码</a>
     * @param qo {@link ReverseGeocodingQO 反向地理编码实体对象}
     * @return 反向地理编码查询结果(与地理编码查询结果相同)
     */
    R<List<GeocodingDTO>> reverseGeocoding(ReverseGeocodingQO qo);

}
