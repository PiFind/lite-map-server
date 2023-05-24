package io.pifind.poi.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * 本地化文本
 */
@Data
public class LocalizedNameGroupDTO {

    /**
     * 目标实体类的主键ID
     */
    private Long id;

    /**
     * 语言-命名，映射
     */
    private Map<String,String> names;

}
