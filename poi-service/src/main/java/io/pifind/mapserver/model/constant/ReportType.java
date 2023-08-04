package io.pifind.mapserver.model.constant;

import java.util.Objects;

/**
 * ReportType
 *
 * @author chenxiaoli
 * @date 2023-07-21
 * @description 举报原因枚举
 */
public enum ReportType {

    LOCATION_ERROR(0, "虚假地理位置"),
    PHONE_ERROR(1, "虚假电话"),
    NOT_SUPPORT_PCM(2, "不支持派支付"),
    POWER_PUBLISH(3, "侵权发布"),
    OTHER(-1, "其他");

    ReportType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private int code;
    private String desc;

    public static ReportType parse(Integer code) {
        if (code == null) {
            return null;
        }
        for (ReportType type : ReportType.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }
}
