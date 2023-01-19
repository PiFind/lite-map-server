package io.pifind.map3rd.google.model.dto;

import lombok.Data;

/**
 * 区域
 */
@Data
public class GoogleRegionDTO {

    /**
     * 西南角坐标
     */
    private GoogleCoordinateDTO southwest;

    /**
     * 东北角坐标
     */
    private GoogleCoordinateDTO northeast;

}
