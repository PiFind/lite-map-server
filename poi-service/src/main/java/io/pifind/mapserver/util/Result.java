package io.pifind.mapserver.util;

import lombok.Data;

import java.util.Objects;

/**
 * Result
 *
 * @author chenxiaoli
 * @date 2023-07-20
 * @description 支付返回对象
 */
@Data
public class Result<T> {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public static boolean isOk(Result result) {
        return Objects.nonNull(result) && Objects.equals(result.getCode(), 0);
    }
}
