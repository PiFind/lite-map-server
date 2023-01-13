package io.pifind.map3rd.google.model.dto;

import lombok.Data;

/**
 * 区域
 */
@Data
public class RegionDTO {

    private WGS84CoordinateDTO southwest;

    private WGS84CoordinateDTO northeast;

}
