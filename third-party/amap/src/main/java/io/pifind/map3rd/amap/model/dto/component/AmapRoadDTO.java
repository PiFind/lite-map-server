package io.pifind.map3rd.amap.model.dto.component;

import lombok.Data;

/**
 * 道路信息
 */
@Data
public class AmapRoadDTO {

    /**
     * 道路信息
     */
    private String id;

    /**
     * 道路名称
     */
    private String name;

    /**
     * 道路到请求坐标的距离 (单位：米)
     */
    private String distance;

    /**
     * 方位
     * <p>输入点和此路的相对方位</p>
     */
    private String direction;

    /**
     * 坐标点
     * <p>经纬度坐标点：经度，纬度</p>
     */
    private String location;

}
