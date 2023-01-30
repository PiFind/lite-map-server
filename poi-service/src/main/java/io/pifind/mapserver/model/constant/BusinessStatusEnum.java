package io.pifind.mapserver.model.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 营业状态
 */
public enum BusinessStatusEnum {

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
