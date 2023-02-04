package io.pifind.mapserver.error;

import io.pifind.common.exception.ServerExceptionCode;
import io.pifind.common.exception.ServerRuntimeException;

public class StringFormatSignException extends ServerRuntimeException {

    public static final int CODE = ServerExceptionCode.LOCAL_TOOL_CLASS_EXCEPTION | 0x00A2;

    public StringFormatSignException(String message) {
        super(CODE, message);
    }

}
