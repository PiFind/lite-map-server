package io.pifind.poi.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 类别
 */
@Data
public class CategoryEditDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 类别名
     */
    private String name;

    /**
     * 父id
     */
    private String parentId;

    /**
     * code
     */
    private Integer code;

    /**
     * 类别名的英文名
     */
    private String nameEN;

    /**
     * 上级分类的ID
     */
    private Long superior;

    /**
     * 该类目下的所有子类
     */
    private List<CategoryEditDTO> categories;

}
