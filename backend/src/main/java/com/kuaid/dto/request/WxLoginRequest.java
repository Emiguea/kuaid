package com.kuaid.dto.request;

import javax.validation.constraints.NotBlank;

public class WxLoginRequest {

    @NotBlank(message = "微信登录code不能为空")
    private String code;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
