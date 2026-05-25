package com.kuaid.enums;

public enum OrderStatusEnum {
    PENDING(0, "待接单"),
    ACCEPTED(1, "已接单"),
    DELIVERING(2, "配送中"),
    COMPLETED(3, "已完成"),
    CANCELLED(4, "已取消");

    private final int code;
    private final String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() { return code; }
    public String getDesc() { return desc; }
}
