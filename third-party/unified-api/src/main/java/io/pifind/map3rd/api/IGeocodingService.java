package io.pifind.map3rd.api;

import io.pifind.common.response.R;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.map3rd.model.GeocodingDTO;
import io.pifind.map3rd.model.ReverseGeocodingDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface IGeocodingService {

    /**
     * 地理编码
     * @param address 地址
     * @return 定位信息
     */
    R<GeocodingDTO> geocoding(@NotEmpty String address);

    /**
     * 反向地理编码
     * @param coordinate 坐标
     * @return 反向地理编码信息
     */
    R<ReverseGeocodingDTO> reverseGeocoding(@NotNull CoordinateDTO coordinate);

}
