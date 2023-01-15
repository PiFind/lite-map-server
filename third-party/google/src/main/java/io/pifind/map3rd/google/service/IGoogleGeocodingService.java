package io.pifind.map3rd.google.service;

import io.pifind.common.response.R;
import io.pifind.map3rd.error.ThirdPartMapServiceException;
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
     * @throws ThirdPartMapServiceException 第三方地图服务调用异常
     */
    R<List<GoogleGeocodingDTO>> geocoding(@NotNull GeocodingQO qo) throws ThirdPartMapServiceException;

    /**
     * 反向地理编码，参考：
     * <a href="https://developers.google.com/maps/documentation/geocoding/requests-reverse-geocoding">Google API 反向地理编码</a>
     * @param qo {@link ReverseGeocodingQO 反向地理编码实体对象}
     * @return 反向地理编码查询结果(与地理编码查询结果相同)
     * @throws ThirdPartMapServiceException 第三方地图服务调用异常
     */
    R<List<GoogleGeocodingDTO>> reverseGeocoding(@NotNull ReverseGeocodingQO qo) throws ThirdPartMapServiceException;

}
