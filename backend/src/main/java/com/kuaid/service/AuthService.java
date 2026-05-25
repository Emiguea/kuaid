package com.kuaid.service;

import com.kuaid.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse wxLogin(String code);

    String refreshToken(String refreshToken);

    void logout(Long userId, String accessToken);
}
