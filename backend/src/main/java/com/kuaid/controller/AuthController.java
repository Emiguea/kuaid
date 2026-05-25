package com.kuaid.controller;

import com.kuaid.dto.request.WxLoginRequest;
import com.kuaid.dto.response.ApiResponse;
import com.kuaid.dto.response.LoginResponse;
import com.kuaid.security.SecurityUser;
import com.kuaid.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/wx-login")
    public ApiResponse<LoginResponse> wxLogin(@Validated @RequestBody WxLoginRequest request) {
        LoginResponse response = authService.wxLogin(request.getCode());
        return ApiResponse.success(response);
    }

    @PostMapping("/refresh")
    public ApiResponse<Map<String, String>> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        String newAccessToken = authService.refreshToken(refreshToken);
        return ApiResponse.success(Map.of("accessToken", newAccessToken));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@AuthenticationPrincipal SecurityUser user,
                                    HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        authService.logout(user.getUserId(), token);
        return ApiResponse.success();
    }
}
