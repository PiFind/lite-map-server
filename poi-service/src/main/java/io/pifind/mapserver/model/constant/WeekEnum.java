package io.pifind.mapserver.model.constant;

/**
 * 星期枚举
 */
public enum WeekEnum {

    /**
     * 星期一
     */
    MONDAY(0x01),

    /**
     * 星期二
     */
    TUESDAY(0x02),

    /**
     * 星期三
     */
    WEDNESDAY(0x04),

    /**
     * 星期四
     */
    THURSDAY(0x08),

    /**
     * 星期五
     */
    FRIDAY(0x10),

    /**
     * 星期六
     */
    SATURDAY(0x20),

    /**
     * 星期日
     */
    SUNDAY(0x20),
    ;

    /**
     * 按位进行的编码
     */
    private final int code;

    private WeekEnum(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

}
