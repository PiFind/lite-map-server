package io.pifind.map3rd.error;

import io.pifind.common.exception.ServerExceptionCode;
import io.pifind.common.response.R;

import static io.pifind.common.exception.ServerExceptionCode.THIRD_PART_SERVICE_EXCEPTION;

/**
 * 第三方 Map API 的错误码
 * <p>
 *     这是一个标准化的返回码，所有第三方的返回错误码/状态码都将用以下
 *     提供的错误码进行标识，而 {@link R#getMessage()} 则需要用第
 *     三方返回的值进行替代，其具体格式为：
 *     <ul>
 *         <li>第三方平台返回的错误信息：{@code message = "第三方平台名:第三方错误码" }</li>
 *         <li>访问失败返回信息({@link MapApiCode#CONNECTION_ERROR})：直接抛出{@link ThirdPartMapServiceException}</li>
 *         <li>第三方服务不支持某功能{@link MapApiCode#UNSUPPORTED_SERVICE_ERROR}: 直接抛出{@link ThirdPartMapServiceException}</li>
 *     </ul>
 * </p>
 * @see ThirdPartMapServiceException
 */
public interface MapApiCode {

    /**
     * 第三方认证错误
     */
    int AUTHENTICATION_ERROR = THIRD_PART_SERVICE_EXCEPTION | 0xB100;

    /**
     * 参数错误
     */
    int PARAMETER_ERROR = THIRD_PART_SERVICE_EXCEPTION | 0xB200;

    /**
     * 签名错误
     */
    int SIGNATURE_ERROR = THIRD_PART_SERVICE_EXCEPTION | 0xB300;

    /**
     * 连接错误
     */
    int CONNECTION_ERROR = THIRD_PART_SERVICE_EXCEPTION | 0xB400;

    /**
     * 服务器拒绝服务错误
     */
    int SERVER_REJECT_ERROR = THIRD_PART_SERVICE_EXCEPTION | 0xB500;

    /**
     * 无效结果
     */
    int INVALID_RESULT = THIRD_PART_SERVICE_EXCEPTION | 0xB600;

    /**
     * 超出服务商给的配额
     */
    int QUOTA_LIMIT_EXCEEDED = THIRD_PART_SERVICE_EXCEPTION | 0xB700;

    /**
     * 服务不支持
     */
    int UNSUPPORTED_SERVICE_ERROR = THIRD_PART_SERVICE_EXCEPTION | 0xB800;

    /**
     * 未知错误
     */
    int UNKNOWN_ERROR = THIRD_PART_SERVICE_EXCEPTION | 0xBFFF;

}
