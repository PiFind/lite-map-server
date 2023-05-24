package io.pifind.poi.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 兴趣点状态
 * <ol start="0">
 *     <li>UNVERIFIED - 未验证</li>
 *     <li>VERIFIED - 已验证</li>
 *     <li>INVALID - 无效</li>
 * </ol>
 */
public enum PoiStatusEnum {

    /**
     * 未验证
     */
    UNVERIFIED(0),

    /**
     * 已验证
     */
    VERIFIED(1),

    /**
     * 无效
     */
    INVALID(2),
    ;

    private final int code;

    private static Map<Integer,PoiStatusEnum> map;

    private PoiStatusEnum(int code) {
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
     * @return {@link PoiStatusEnum 状态枚举对象}
     */
    public static PoiStatusEnum valueOf(int code) {
        if (map == null) {
            map = new HashMap<>();
            for (PoiStatusEnum status : values()) {
                map.put(status.code(), status);
            }
        }
        return map.get(code);
    }

}
