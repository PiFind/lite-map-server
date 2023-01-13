package io.pifind.map3rd.google.model.constant;

/**
 * 地理编码Wrapper中可能返回的状态值
 * <a href="https://developers.google.com/maps/documentation/geocoding/requests-geocoding#StatusCodes">
 * 状态代码
 * </a>
 */
public enum GeocodingStatusEnum {

    /**
     * 表示未出现任何错误，并且至少返回了一个地址。
     */
    OK,

    /**
     * 表示地理编码成功，但未返回任何结果。如果向地理编码器传递了不存在的 address，就可能会发生这种情况。
     */
    ZERO_RESULTS,

    /**
     * 表示以下任一情况：
     * <ul>
     *     <li>API 密钥缺失或无效。</li>
     *     <li>您的帐号尚未启用结算功能。</li>
     *     <li>超出了您设定的用量上限。</li>
     *     <li>提供的付款方式不再有效（例如，信用卡已过期）。</li>
     * </ul>
     * 请参阅 <a href="https://developers.google.com/maps/faq#over-limit-key-error">Google 地图常见问题解答</a>，了解如何解决此问题。
     */
    OVER_DAILY_LIMIT,

    /**
     * 表示您超出了配额。
     */
    OVER_QUERY_LIMIT,

    /**
     * 表示您的请求已遭拒。
     */
    REQUEST_DENIED,

    /**
     * 通常表示缺少查询参数（address、components 或 latlng）。
     */
    INVALID_REQUEST,

    /**
     * 表示因服务器错误而无法处理该请求。如果您重试一次，请求可能会成功。
     */
    UNKNOWN_ERROR,
}
