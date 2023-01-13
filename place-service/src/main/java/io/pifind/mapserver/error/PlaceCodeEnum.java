package io.pifind.mapserver.error;

import io.pifind.common.annotation.ErrorCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 地点错误码
 */
@ErrorCode
public enum PlaceCodeEnum {

    /**
     * 未找到行政区
     */
    ADMINISTRATIVE_AREA_NOT_FOUND(0x1001,"Place.AdministrativeAreaNotFound"),
    /**
     * 未能定位到行政区
     */
    FAILED_TO_LOCATE_TO_ADMINISTRATIVE_AREA(0x1A01,"Place.FailedToLocateToAdministrativeArea")
    ;

    private final int code;
    private final String messageId;

    private static Map<Integer,PlaceCodeEnum> map;

    PlaceCodeEnum(int code, String messageId) {
        this.code = code;
        this.messageId = messageId;
    }

    public int code() {
        return code;
    }

    public String messageId() {
        return messageId;
    }

    public static PlaceCodeEnum valueOf(int code) {
        if (map == null) {
            map = new HashMap<>();
            for (PlaceCodeEnum placeCode : values()) {
                map.put(placeCode.code(),placeCode);
            }
        }
        return map.get(code);
    }

}
