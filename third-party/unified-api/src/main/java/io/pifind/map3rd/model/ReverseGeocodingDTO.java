package io.pifind.map3rd.model;

import lombok.Data;

import java.util.Comparator;
import java.util.List;

/**
 * 标准反向地理编码实体类
 */
@Data
public class ReverseGeocodingDTO {

    /**
     * 定位结果组合名
     */
    private String fullName;

    /**
     * 行政区划
     * <p>
     *     按照 level 从小到大进行排序
     * </p>
     */
    private List<SingleDistrictDTO> districts;

    /**
     * 默认区划排序器
     */
    public static final Comparator<SingleDistrictDTO> DISTRICT_COMPARATOR =
            (o1,o2) -> o1.getLevel() - o2.getLevel();

}
