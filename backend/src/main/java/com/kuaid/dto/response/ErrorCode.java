package com.kuaid.dto.response;

public enum ErrorCode {

    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    // Auth 1xxx
    WX_LOGIN_FAILED(1001, "微信登录失败"),
    TOKEN_INVALID(1002, "令牌无效"),
    TOKEN_EXPIRED(1003, "令牌已过期"),
    REFRESH_TOKEN_INVALID(1004, "刷新令牌无效"),

    // User 2xxx
    USER_NOT_FOUND(2001, "用户不存在"),
    USER_DISABLED(2002, "用户已被禁用"),
    PHONE_ALREADY_BOUND(2003, "手机号已被绑定"),

    // Express 3xxx
    EXPRESS_NOT_FOUND(3001, "快递不存在"),
    PICKUP_CODE_INVALID(3002, "取件码错误"),
    EXPRESS_ALREADY_PICKED(3003, "快递已被取走"),
    EXPRESS_EXPIRED(3004, "快递已过期"),
    TRACKING_NO_DUPLICATE(3005, "快递单号已存在"),

    // Order 4xxx
    ORDER_NOT_FOUND(4001, "订单不存在"),
    ORDER_CANNOT_CANCEL(4002, "订单无法取消"),
    ORDER_ALREADY_ACCEPTED(4003, "订单已被接单"),
    INSUFFICIENT_BALANCE(4004, "余额不足"),

    // Station 5xxx
    STATION_NOT_FOUND(5001, "站点不存在"),
    STATION_CLOSED(5002, "站点已关闭");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
}
