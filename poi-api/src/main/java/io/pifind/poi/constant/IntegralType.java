package io.pifind.poi.constant;

import java.util.Objects;

/**
 * IntegralType
 *
 * @author chenxiaoli
 * @date 2023-07-26
 * @description type类型
 */
public enum IntegralType {

    INVITE("invite", "邀请"),
    DAO_AUDIT("DAOAudit", "DAO审核"),
    VIEW_PHONE("viewPhone", "查看电话"),
    COMMENT("comment", "评论"),
    DAO_VOTE("DAOVote", "DAO投票"),
    ;

    IntegralType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private String code;
    private String desc;

    public static IntegralType parse(String code) {
        if (code == null) {
            return null;
        }
        for (IntegralType type : IntegralType.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }
}
