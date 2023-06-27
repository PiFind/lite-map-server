package io.pifind.mapserver.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 兴趣点收藏
 *
 * @author chenxiaoli
 * @date 2023-06-27
 * @description
 */
@Data
@TableName(value = "interest_point_collect",autoResultMap = true)
public class InterestPointCollectPO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * [必填]用户名
     */
    private String username;

    /**
     * [必填]兴趣点ID
     */
    @TableField("interest_point_id")
    private Long interestPointId;

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
     * 逻辑删除字段
     */
    private Boolean unavailable;
}
