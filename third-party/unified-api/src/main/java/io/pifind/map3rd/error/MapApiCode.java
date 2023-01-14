package io.pifind.map3rd.error;

import io.pifind.common.response.R;

/**
 * 第三方 Map API 的错误码
 * <p>
 *     这是一个标准化的返回码，所有第三方的返回错误码/状态码都将用以下
 *     提供的错误码进行标识，而 {@link R#getMessage()} 则需要用第
 *     三方返回的值进行替代，其具体格式为：
 *     <p>
 *         {@code message = "第三方平台:第三方错误码" }
 *     </p>
 * </p>
 */
public interface MapApiCode {

    /**
     * 第三方认证错误
     */
    int AUTHENTICATION_ERROR = 0xB001;

    /**
     * 参数错误
     */
    int PARAMETER_ERROR = 0xB002;

    /**
     * 签名错误
     */
    int SIGNATURE_ERROR = 0xB003;

    /**
     * 连接错误
     */
    int CONNECTION_ERROR = 0xB004;

    /**
     * 服务器拒绝错误
     */
    int SERVER_REJECT_ERROR = 0xB005;

    /**
     * 无效结果
     */
    int INVALID_RESULT = 0xB006;

    /**
     * 超出服务商给的配额
     */
    int QUOTA_LIMIT_EXCEEDED = 0xB007;

    /**
     * 未知错误
     */
    int UNKNOWN_ERROR = 0xBFFF;

}
