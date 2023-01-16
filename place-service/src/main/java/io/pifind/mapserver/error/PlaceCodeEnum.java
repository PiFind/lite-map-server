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
    ADMINISTRATIVE_AREA_NOT_FOUND(0x100001,"Place.AdministrativeAreaNotFound"),
    /**
     * 未能定位到行政区
     */
    FAILED_TO_LOCATE_TO_ADMINISTRATIVE_AREA(0x110002,"Place.FailedToLocateToAdministrativeArea"),
    /**
     * 不支持的的坐标系
     */
    UNSUPPORTED_COORDINATE_SYSTEM(0x110003,"Place.UnresolvedCoordinateSystem"),
    /**
     * 错误的 IPv4 地址
     */
    WRONG_IPV4_ADDRESS(0x110004,"Place.WrongIPv4Address"),
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
