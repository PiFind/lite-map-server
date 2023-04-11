package io.pifind.mapserver.model.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum CoordinateSystemEnum {

    /**
     * GPS 用的坐标系
     */
    WGS84(0),

    /**
     * 由中国国家测绘局制定的地理坐标系统。
     */
    GCJ02(1),

    /**
     * 中国北斗系统所使用的坐标系。
     */
    CGCS2000(2),

    ;

    @EnumValue
    private final int code;

    private CoordinateSystemEnum(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
