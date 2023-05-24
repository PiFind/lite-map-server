package io.pifind.poi.constant;

import java.util.HashMap;
import java.util.Map;

public enum PoiCommentStatusEnum {

    /**
     * 未审核
     */
    UNVERIFIED(0),

    /**
     * 已审核
     */
    VERIFIED(1),

    /**
     * 无效
     */
    INVALID(2),

    ;

    private final int code;

    private static Map<Integer,PoiCommentStatusEnum> map;

    private PoiCommentStatusEnum(int code) {
        this.code = code;
    }

    /**
     * 获取状态值，每一状态都对应着唯一的值
     * @return 状态值
     */
    public int code() {
        return code;
    }

    /**
     * 根据状态值获取状态枚举对象
     * @param code 状态值
     * @return {@link PoiCommentStatusEnum 状态枚举对象}
     */
    public static PoiCommentStatusEnum valueOf(int code) {
        if (map == null) {
            map = new HashMap<>();
            for (PoiCommentStatusEnum status : values()) {
                map.put(status.code(), status);
            }
        }
        return map.get(code);
    }

}
