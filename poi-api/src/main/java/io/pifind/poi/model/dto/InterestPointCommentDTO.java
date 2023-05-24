package io.pifind.poi.model.dto;

import lombok.Data;

/**
 * 兴趣点评论
 */
@Data
public class InterestPointCommentDTO {

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 兴趣点 ID
     */
    private Long interestPointId;

    /**
     * 上级评论 ID
     */
    private Long superiorId;

    /**
     * 评论内容
     */
    private String content;

}
