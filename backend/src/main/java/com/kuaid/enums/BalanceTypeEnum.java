package com.kuaid.enums;

public enum BalanceTypeEnum {
    RECHARGE(0, "充值"),
    PAYMENT(1, "支付"),
    REFUND(2, "退款"),
    INCOME(3, "收入");

    private final int code;
    private final String desc;

    BalanceTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() { return code; }
    public String getDesc() { return desc; }
}
