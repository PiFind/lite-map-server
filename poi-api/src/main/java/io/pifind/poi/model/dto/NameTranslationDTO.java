package io.pifind.poi.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 命名翻译实体对象
 */
@Data
public class NameTranslationDTO {

    /**
     * 目标实体类的主键ID
     */
    private Long id;

    /**
     * 翻译的语言
     */
    private List<String> languages;

}
