package io.pifind.mapserver.model.po;

import com.baomidou.mybatisplus.annotation.*;
import io.pifind.mapserver.mp.type.LocalizedNameTypeHandler;
import lombok.Data;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Data
@TableName(value = "category",autoResultMap = true)
public class CategoryPO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 本地名
     */
    @TableField(typeHandler = LocalizedNameTypeHandler.class)
    private Map<Locale,String> names;

    /**
     * 英文名
     */
    @TableField("name_en")
    private String nameEN;

    /**
     * 上级ID
     */
    private Long superior;

    /**
     * 层级
     */
    private int level;

    /**
     * [自动]创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * [自动]更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * [自动]逻辑删除字段
     */
    @TableLogic
    private Boolean unavailable;

}
