package io.pifind.poi.model.dto;

import lombok.Data;

/**
 * 评论审核
 */
@Data
public class DaoCommentModerationDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 兴趣点 ID
     */
    private Long InterestPointCommentId;

    /**
     * 是否通过
     */
    private Boolean pass;

    /**
     * 备注
     */
    private String note;

}
