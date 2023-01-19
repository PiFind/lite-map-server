package io.pifind.map3rd.amap.model.wrapper;

import lombok.Data;
import io.pifind.map3rd.amap.model.constant.InfoCodeEnum;
import io.pifind.map3rd.amap.model.constant.ResponseStatusEnum;

/**
 * 高德地图开放平台响应包装类
 * @param <T> 被包装的返回值数据类型
 */
@Data
public class AmapResponseWrapper<T> {

    /**
     * 返回结果状态值
     * <p>返回值为 0 或 1，0 表示请求失败；1 表示请求成功。</p>
     * @see ResponseStatusEnum
     */
    private String status;

    /**
     * 返回状态说明
     * <p>当 status 为 0 时，info 会返回具体错误原因，否则返回“OK”。</p>
     * @see InfoCodeEnum
     */
    private String info;

    /**
     * 包装的返回值结果
     */
    private T result;

}
