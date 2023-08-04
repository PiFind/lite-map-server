package io.pifind.mapserver.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * InterestPointReportPO
 *
 * @author chenxiaoli
 * @date 2023-07-21
 * @description 举报
 */
@Data
@TableName(value = "interest_point_report",autoResultMap = true)
public class InterestPointReportPO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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
     * 举报内容
     */
    private String content;

    /**
     * [自动]创建时间
     */
    private Date createTime;

    /**
     * [自动]更新时间
     */
    private Date updateTime;
}
