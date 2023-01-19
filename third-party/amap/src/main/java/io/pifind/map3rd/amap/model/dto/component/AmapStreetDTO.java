package io.pifind.map3rd.amap.model.dto.component;

import lombok.Data;

/**
 * 门牌信息
 */
@Data
public class AmapStreetDTO {

    /**
     * 街道名称
     */
    private String street;

    /**
     * 门牌号
     */
    private String number;

    /**
     * 坐标点
     * <p>经纬度坐标点：经度，纬度</p>
     */
    private String location;

    /**
     * 方向
     * <p>坐标点所处街道方位</p>
     */
    private String direction;

    /**
     * 门牌地址到请求坐标的距离（单位：米）
     */
    private String distance;

}
