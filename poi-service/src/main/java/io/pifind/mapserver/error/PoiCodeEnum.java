package io.pifind.mapserver.error;

import io.pifind.common.annotation.ErrorCode;

import java.util.HashMap;
import java.util.Map;

@ErrorCode
public enum PoiCodeEnum {

    DUPLICATE_POI_DATA(0x210001,"Poi.DuplicatePoiData"),
    POI_DATA_NOT_FOUND(0x210002,"Poi.PoiDataNotFound"),
    POI_DATA_OWNER(0x210003,"Poi.PoiDataOwner"),
    ;

    private final int code;
    private final String message;

    private static Map<Integer,PoiCodeEnum> map;

    PoiCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public static PoiCodeEnum valueOf(int code) {
        if (map == null) {
            map = new HashMap<>();
            for (PoiCodeEnum poiCode : values()) {
                map.put(poiCode.code(),poiCode);
            }
        }
        return map.get(code);
    }
}
