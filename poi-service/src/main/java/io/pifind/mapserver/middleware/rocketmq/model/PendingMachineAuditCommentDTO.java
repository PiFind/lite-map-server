package io.pifind.mapserver.middleware.rocketmq.model;

import lombok.Data;

import java.util.Date;

/**
 * 等待机审的评论
 */
@Data
public class PendingMachineAuditCommentDTO {

    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 创建时间
     */
    private Date createTime;

}
