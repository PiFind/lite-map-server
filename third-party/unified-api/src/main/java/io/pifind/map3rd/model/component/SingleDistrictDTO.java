package io.pifind.map3rd.model.component;

import lombok.Data;

/**
 * 标准单个行政区实体类
 */
@Data
public class SingleDistrictDTO {

    /**
     * 区域名
     */
    private String name;

    /**
     * 行政区等级
     * <ol start="0">
     *     <li>国家/地区</li>
     *     <li>一级行政区</li>
     *     <li>二级行政区</li>
     *     <li>...</li>
     * </ol>
     */
    private Integer level;

}
