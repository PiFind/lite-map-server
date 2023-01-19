package io.pifind.map3rd.amap.model.dto.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 简化的 POI（Point of Interest） 信息
 */
@Data
public class AmapSimplifiedPoiDTO {

    /**
     * POI 的 ID
     */
    private String id;

    /**
     * POI 名称
     */
    private String name;

    /**
     * POI 类型
     */
    private String type;

    /**
     * 电话
     */
    private String tel;

    /**
     * 该POI的中心点到请求坐标的距离（单位：米）
     */
    private String distance;

    /**
     * 方向
     * <p>为输入点相对建筑物的方位</p>
     */
    private String direction;

    /**
     * POI地址信息
     */
    private String address;

    /**
     * 坐标点
     * <p>经纬度坐标点：经度，纬度</p>
     */
    private String location;

    /**
     * POI所在商圈名称
     */
    @JsonProperty("businessarea")
    private String businessAreaName;

}
