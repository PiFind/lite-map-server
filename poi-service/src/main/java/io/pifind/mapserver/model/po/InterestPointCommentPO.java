package io.pifind.mapserver.model.po;

import com.baomidou.mybatisplus.annotation.*;
import io.pifind.mapserver.model.constant.InterestPointCommentStatusEnum;
import lombok.Data;

import java.util.Date;

@Data
@TableName("interest_point_comment")
public class InterestPointCommentPO {

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
     * [可选]上级ID
     */
    @TableField("superior_id")
    private Long superiorId;

    /**
     * [必填] 行政区域ID
     */
    @TableField("administrative_area_id")
    private Long administrativeAreaId;

    /**
     * [必填]内容
     */
    private String content;

    /**
     * [可选]点赞数
     * <p>
     *     默认为 0
     * </p>
     */
    private int likes;

    /**
     * [必填]状态
     */
    private InterestPointCommentStatusEnum status;

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
