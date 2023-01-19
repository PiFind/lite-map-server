package io.pifind.map3rd.google.model.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Google API 响应包装类
 * @param <T> 被包装的返回值数据类型
 */
@Data
public class GoogleResponseWrapper<T> {

    /**
     * 查询结果
     */
    private List<T> results;

    /**
     * 返回状态编码
     */
    private String status;

    /**
     * 当地理编码器返回的状态代码不是 {@code OK} 时，地理编码响应对象中可能会包含一个
     * 额外的 {@code error_message} 字段。此字段更详细地说明了给定状态代码背后的原因。
     */
    @JsonProperty("error_message")
    private String errorMessage;

}
