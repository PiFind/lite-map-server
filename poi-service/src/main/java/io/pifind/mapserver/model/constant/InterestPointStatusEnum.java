package io.pifind.mapserver.model.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 兴趣点状态
 */
public enum InterestPointStatusEnum {


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
