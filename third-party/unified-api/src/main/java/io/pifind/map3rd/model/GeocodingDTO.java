package io.pifind.map3rd.model;

import io.pifind.map.model.CoordinateDTO;
import lombok.Data;

/**
 * 标准地理编码实体类
 */
@Data
public class GeocodingDTO {

    /**
     * 目标名
     */
    private String name;

    /**
     * 目标位置的坐标
     */
    private CoordinateDTO coordinate;

}
