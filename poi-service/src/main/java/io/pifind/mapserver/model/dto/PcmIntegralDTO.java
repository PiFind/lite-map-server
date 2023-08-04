package io.pifind.mapserver.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * PcmIntegralDTO
 *
 * @author chenxiaoli
 * @date 2023-07-26
 * @description PCM积分实体
 */
@Data
public class PcmIntegralDTO {

    /**
     * 用户名
     */
    @JSONField(name = "user_name")
    private String username;

    /**
     * 用户名
     */
    @JSONField(name = "app_id")
    private Integer appId;

    /**
     * 类型
     */
    private String type;

    /**
     * 签名
     */
    @JSONField(name = "x_token")
    private String xToken;

    /**
     * 时间戳（精确到秒）
     */
    private Long timestamp;
}
