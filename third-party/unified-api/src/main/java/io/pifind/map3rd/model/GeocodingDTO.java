package io.pifind.map3rd.model;

import io.pifind.map3rd.model.component.GeographicalTargetDTO;
import lombok.Data;

import java.util.List;

/**
 * 地理编码
 */
@Data
public class GeocodingDTO {

    /**
     * 目标的数量
     */
    private Integer count;

    /**
     * 找到的目标
     */
    private List<GeographicalTargetDTO> targets;

}
