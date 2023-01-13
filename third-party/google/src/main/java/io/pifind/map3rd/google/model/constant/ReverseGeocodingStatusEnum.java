package io.pifind.map3rd.google.model.constant;

/**
 * 反向地理编码Wrapper中可能返回的状态值，参考
 * <a href="https://developers.google.com/maps/documentation/geocoding/requests-reverse-geocoding#reverse-status-codes">
 * 反向地理编码状态代码
 * </a>
 */
public enum ReverseGeocodingStatusEnum {

    /**
     * 表示未出现任何错误，并且至少返回了一个地址。
     */
    OK,

    /**
     * 表示反向地理编码成功，但未返回任何结果。如果向地理编码器传递了偏远位置的 latlng，就可能会发生这种情况。
     */
    ZERO_RESULTS,

    /**
     * 表示您超出了配额。
     */
    OVER_QUERY_LIMIT,

    /**
     * 表示请求被拒绝。这可能是因为请求包含 result_type 或 location_type 参数，但不包含 API 密钥。
     */
    REQUEST_DENIED,

    /**
     * 通常表示以下状态之一：
     * <ul>
     *     <li>缺少查询（address、components 或 latlng）。</li>
     *     <li>提供的 result_type 或 location_type 无效。</li>
     * </ul>
     */
    INVALID_REQUEST,

    /**
     * 表示因服务器错误而无法处理该请求。如果您重试一次，请求可能会成功。
     */
    UNKNOWN_ERROR,
    ;

}
