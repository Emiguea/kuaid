package com.kuaid.enums;

public enum ExpressStatusEnum {
    PENDING_PICKUP(0, "待取件"),
    PICKED_UP(1, "已取件"),
    EXPIRED(2, "已过期"),
    RETURNED(3, "已退回");

    private final int code;
    private final String desc;

    ExpressStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() { return code; }
    public String getDesc() { return desc; }
}
