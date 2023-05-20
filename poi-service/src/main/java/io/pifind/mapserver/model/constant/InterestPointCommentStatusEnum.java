package io.pifind.mapserver.model.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum InterestPointCommentStatusEnum {

    /**
     * 等待机审
     */
    PENDING (0),

    /**
     * 已经审核通过
     */
    PASS (1),

    /**
     * 未审核通过
     */
    REFUSE (2),

    ;

    @EnumValue
    private final int code;

    InterestPointCommentStatusEnum(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

}
