package com.kuaid.enums;

public enum RoleEnum {
    STUDENT(0, "ROLE_STUDENT"),
    COURIER(1, "ROLE_COURIER");

    private final int code;
    private final String authority;

    RoleEnum(int code, String authority) {
        this.code = code;
        this.authority = authority;
    }

    public int getCode() { return code; }
    public String getAuthority() { return authority; }

    public static RoleEnum fromCode(int code) {
        for (RoleEnum role : values()) {
            if (role.code == code) return role;
        }
        throw new IllegalArgumentException("Unknown role code: " + code);
    }
}
