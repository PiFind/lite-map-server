package io.pifind.map3rd.google.model.dto;

import lombok.Data;

/**
 * Google 坐标实体类
 * <p>Google 使用的是 WGS84 坐标</p>
 */
@Data
public class GoogleCoordinateDTO {

    /**
     * 维度
     */
    private Double lat;

    /**
     * 经度
     */
    private Double lng;

}
