package io.pifind.poi.model.dto;

import lombok.Data;

/**
 * 本地化命名
 */
@Data
public class LocalizedNameDTO {

    /**
     * 目标实体类的主键ID
     */
    private Long id;

    /**
     * 语言
     */
    private String language;

    /**
     * 本地命名
     */
    private String name;

}
