package io.pifind.poi.model.dto;

import lombok.Data;

/**
 * InterestPointReportDTO
 *
 * @author chenxiaoli
 * @date 2023-07-21
 * @description 兴趣点举报
 */
@Data
public class InterestPointReportDTO {

    /**
     * 举报人用户名
     */
    private String username;

    /**
     * 举报人id
     */
    private Long reportId;

    /**
     * 被举报人用户名
     */
    private String reportedUsername;

    /**
     * 被举报人id
     */
    private Long reportedId;

    /**
     * 举报原因
     */
    private Integer type;

    /**
     * 举报原因
     */
    private String content;
}
