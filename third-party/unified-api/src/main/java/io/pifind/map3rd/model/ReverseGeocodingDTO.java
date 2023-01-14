package io.pifind.map3rd.model;

import lombok.Data;

import java.util.List;

@Data
public class ReverseGeocodingDTO {

    /**
     * 定位结果组合名
     */
    private String fullName;

    /**
     * 行政区划
     */
    private List<SingleDistrictDTO> districts;

}
