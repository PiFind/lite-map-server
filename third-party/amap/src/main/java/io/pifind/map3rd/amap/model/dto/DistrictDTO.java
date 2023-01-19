package io.pifind.map3rd.amap.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DistrictDTO {

    /**
     * 城市编码
     */
    private List<String> citycode;

    /**
     * 区域编码
     * <p>街道没有独有的adcode，均继承父类（区县）的adcode</p>
     */
    private String adcode;

    /**
     * 行政区名称
     */
    private String name;

    /**
     * 行政区边界坐标点
     * <p>当一个行政区范围，由完全分隔两块或者多块的地块组成，
     * 每块地的 polyline 坐标串以 | 分隔 。</p>
     * <p>如北京 的 朝阳区</p>
     */
    private String polyline;

    /**
     * 区域中心点
     */
    private String center;

    /**
     * 行政区划级别
     * <ol>
     *     <li>country:国家</li>
     *     <li>province:省份（直辖市会在province和city显示）</li>
     *     <li>city:市（直辖市会在province和city显示）</li>
     *     <li>district:区县</li>
     *     <li>street:街道</li>
     * </ol>
     */
    private String level;

    /**
     * 下级行政区列表，包含district元素
     */
    private List<DistrictDTO> districts;

}
