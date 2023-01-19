package io.pifind.map3rd.amap.model.constant;

public enum ResponseStatusEnum {

    SUCCESS("1"),
    FAILURE("0");

    private String code;

    ResponseStatusEnum(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

}
