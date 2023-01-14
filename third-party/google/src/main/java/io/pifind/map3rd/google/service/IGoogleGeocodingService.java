package io.pifind.map3rd.google.service;

import io.pifind.common.response.R;
import io.pifind.map3rd.google.model.dto.GoogleGeocodingDTO;
import io.pifind.map3rd.google.model.qo.GeocodingQO;
import io.pifind.map3rd.google.model.qo.ReverseGeocodingQO;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 地理编码服务
 */
public interface IGoogleGeocodingService {

    /**
     * 地理编码，参考：
     * <a href="https://developers.google.com/maps/documentation/geocoding/requests-geocoding">Google API 地理编码</a>
     * @param qo {@link GoogleGeocodingDTO 地理编码实体对象}
     * @return 地理编码查询结果
     */
    R<List<GoogleGeocodingDTO>> geocoding(@NotNull GeocodingQO qo);

    /**
     * 反向地理编码，参考：
     * <a href="https://developers.google.com/maps/documentation/geocoding/requests-reverse-geocoding">Google API 反向地理编码</a>
     * @param qo {@link ReverseGeocodingQO 反向地理编码实体对象}
     * @return 反向地理编码查询结果(与地理编码查询结果相同)
     */
    R<List<GoogleGeocodingDTO>> reverseGeocoding(@NotNull ReverseGeocodingQO qo);

}
