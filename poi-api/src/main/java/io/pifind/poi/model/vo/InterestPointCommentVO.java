package io.pifind.poi.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.pifind.poi.constant.PoiCommentStatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 兴趣点评论
 */
@Data
public class InterestPointCommentVO {

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 兴趣点ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long interestPointId;

    /**
     * 上级ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long superiorId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private int likes;

    /**
     * 评论状态
     */
    private PoiCommentStatusEnum status;

    /**
     * 子评论
     */
    private List<InterestPointCommentVO> subComments;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
