package io.pifind.map3rd.google.model.wrapper;

import io.pifind.map3rd.google.model.dto.GoogleGeocodingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 地理编码/反向地理编码返回值实体类的包装类
 * @see GoogleGeocodingDTO
 */
@EqualsAndHashCode(callSuper = true)
public class GeocodingWrapper extends GoogleResponseWrapper<GoogleGeocodingDTO> {

}
