package io.pifind.poi.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类别
 */
@Data
public class CategoryVO {

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 类别名
     */
    private String name;

    /**
     * 类别名的英文名
     */
    private String nameEN;

    /**
     * 上级分类的ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long superior;

    /**
     * 该类目下的所有子类
     */
    private List<CategoryVO> categories;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
