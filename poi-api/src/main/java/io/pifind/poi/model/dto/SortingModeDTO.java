package io.pifind.poi.model.dto;

import io.pifind.poi.constant.SortOrderEnum;
import io.pifind.poi.constant.SortReferenceEnum;
import lombok.Data;

/**
 * 排序模式
 */
@Data
public class SortingModeDTO {

    /**
     * 排序顺序（升序/降序）
     */
    private SortOrderEnum order;

    /**
     * 排序需要参考的字段
     */
    private SortReferenceEnum reference;

}
