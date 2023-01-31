package io.pifind.mapserver.model.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 兴趣点状态
 */
public enum InterestPointStatusEnum {

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

    @EnumValue
    private final int code;

    private InterestPointStatusEnum(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

}
