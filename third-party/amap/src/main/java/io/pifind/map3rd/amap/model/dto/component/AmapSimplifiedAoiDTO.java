package io.pifind.map3rd.amap.model.dto.component;

import lombok.Data;

/**
 * 简化的 AOI（Area of Interest） 信息
 */
@Data
public class AmapSimplifiedAoiDTO {

    /**
     * AOI 的 ID
     */
    private String id;

    /**
     * AOI 名称
     */
    private String name;

    /**
     * 所属 AOI 所在区域编码
     */
    private String adcode;

    /**
     * 所属 AOI 中心点坐标
     * <p>经纬度坐标点：经度，纬度</p>
     */
    private String location;

    /**
     * 所属 AOI 点面积
     * <p>单位：平方米</p>
     */
    private String area;

    /**
     * 输入经纬度是否在 AOI 面之中
     * <p> 0，代表在aoi内</p>
     * <p>其余整数代表距离AOI的距离</p>
     */
    private String distance;

    /**
     * 所属 AOI 类型
     */
    private String type;

}
