package io.pifind.map3rd.google.model.dto;

import lombok.Data;

@Data
public class WGS84CoordinateDTO {

    /**
     * 维度
     */
    private Double lat;

    /**
     * 经度
     */
    private Double lng;

}
