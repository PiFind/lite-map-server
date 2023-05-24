package io.pifind.poi.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 公司状态
 * <ol start="0">
 *     <li>PENDING_REVIEW - 等待审核的</li>
 *     <li>APPROVED - 通过审核的</li>
 *     <li>UNAPPROVED - 未通过审核的</li>
 *     <li>INVALID - 无效</li>
 * </ol>
 */
public enum CompanyStatusEnum {

    /**
     * 等待审核的
     */
    PENDING_REVIEW(0),

    /**
     * 通过审核的
     */
    APPROVED(1),

    /**
     * 未通过审核的
     */
    UNAPPROVED(2),

    /**
     * 无效
     */
    INVALID(3),
    ;

    private final int code;

    private static Map<Integer,CompanyStatusEnum> map;

    private CompanyStatusEnum(int code) {
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
     * @return {@link CompanyStatusEnum 状态枚举对象}
     */
    public static CompanyStatusEnum valueOf(int code) {
        if (map == null) {
            map = new HashMap<>();
            for (CompanyStatusEnum status : values()) {
                map.put(status.code(), status);
            }
        }
        return map.get(code);
    }


}
