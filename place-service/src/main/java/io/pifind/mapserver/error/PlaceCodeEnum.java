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
    ADMINISTRATIVE_AREA_NOT_FOUND(0x1001,"Place.AdministrativeAreaNotFounds"),
    /**
     * 未能定位到行政区
     */
    FAILED_TO_LOCATE_TO_ADMINISTRATIVE_AREA(0x1A01,"Place.FailedToLocateToAdministrativeArea"),
    /**
     * 无法解析的坐标系
     */
    UNRESOLVED_COORDINATE_SYSTEM(0x1A02,"Place.UnresolvedCoordinateSystem"),
    ;

    private final int code;
    private final String message;

    private static Map<Integer,PlaceCodeEnum> map;

    PlaceCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
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
