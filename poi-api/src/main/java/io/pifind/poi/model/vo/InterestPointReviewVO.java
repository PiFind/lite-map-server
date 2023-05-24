package io.pifind.poi.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * InterestPointReviewVO
 *
 * @author chenxiaoli
 * @date 2023-05-24
 * @description
 */
@Data
public class InterestPointReviewVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * [必填]用户名
     */
    private String username;

    /**
     * [必填]兴趣点ID
     */
    private Long interestPointId;

    /**
     * [必填]是否同意
     */
    private Boolean agree;

    /**
     * [自动]创建时间
     */
    private Date createTime;

    /**
     * [自动]更新时间
     */
    private Date updateTime;

    /**
     * [自动]逻辑删除字段
     */
    private Boolean unavailable;
}
