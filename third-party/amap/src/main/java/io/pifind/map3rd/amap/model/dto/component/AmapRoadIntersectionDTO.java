package io.pifind.map3rd.amap.model.dto.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 道路交叉口
 */
@Data
public class AmapRoadIntersectionDTO {

    /**
     * 交叉路口到请求坐标的距离（单位：米）
     */
    private String distance;

    /**
     * 方位
     * <p>输入点相对路口的方位</p>
     */
    private String direction;

    /**
     * 路口经纬度
     * <p>经纬度坐标点：经度，纬度</p>
     */
    private String location;

    /**
     * 第一条道路ID
     */
    @JsonProperty("first_id")
    private String firstRoadId;

    /**
     * 第一条道路名称
     */
    @JsonProperty("first_name")
    private String firstRoadName;

    /**
     * 第二条道路ID
     */
    @JsonProperty("second_id")
    private String secondRoadId;

    /**
     * 第二条道路名称
     */
    @JsonProperty("second_name")
    private String secondRoadName;

}
