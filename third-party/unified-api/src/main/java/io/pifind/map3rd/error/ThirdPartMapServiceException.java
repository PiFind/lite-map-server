package io.pifind.map3rd.error;

import io.pifind.common.exception.ServerRuntimeException;

/**
 * 第三方地图服务异常
 */
public class ThirdPartMapServiceException extends ServerRuntimeException {

    public ThirdPartMapServiceException(int code, String message) {
        super(code, message);
    }

}
