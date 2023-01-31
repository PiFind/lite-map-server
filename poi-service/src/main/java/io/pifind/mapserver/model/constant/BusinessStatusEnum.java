package io.pifind.mapserver.model.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 营业状态
 */
public enum BusinessStatusEnum {
    /**
     * 正常
     */
    NORMAL(0),

    /**
     * 休店
     */
    CLOSE(1),

    /**
     * 倒闭破产
     */
    BANKRUPT(2),

    ;

    @EnumValue
    private final int code;

    private BusinessStatusEnum(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
