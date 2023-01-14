package io.pifind.map3rd.google.model.dto;

import lombok.Data;

/**
 * 区域
 */
@Data
public class GoogleRegionDTO {

    private GoogleCoordinateDTO southwest;

    private GoogleCoordinateDTO northeast;

}
