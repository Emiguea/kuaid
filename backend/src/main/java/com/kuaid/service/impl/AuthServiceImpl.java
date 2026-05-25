package com.kuaid.service.impl;

import com.kuaid.dao.UserMapper;
import com.kuaid.dto.response.ErrorCode;
import com.kuaid.dto.response.LoginResponse;
import com.kuaid.entity.User;
import com.kuaid.enums.RoleEnum;
import com.kuaid.exception.BusinessException;
import com.kuaid.security.JwtTokenProvider;
import com.kuaid.service.AuthService;
import com.kuaid.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private WechatUtil wechatUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public LoginResponse wxLogin(String code) {
        Map<String, String> wxResult = wechatUtil.code2Session(code);
        if (wxResult.containsKey("error")) {
            throw new BusinessException(ErrorCode.WX_LOGIN_FAILED);
        }

        String openid = wxResult.get("openid");
        String unionId = wxResult.get("unionid");

        User user = userMapper.selectByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setUnionId(unionId);
            user.setRole(RoleEnum.STUDENT.getCode());
            user.setStatus(1);
            userMapper.insert(user);
        }

        if (user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }

        RoleEnum role = RoleEnum.fromCode(user.getRole());
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), role);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUserId(user.getId());
        response.setRole(user.getRole());
        response.setNickname(user.getNickname());
        response.setAvatarUrl(user.getAvatarUrl());
        return response;
    }

    @Override
    public String refreshToken(String refreshToken) {
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        if (!jwtTokenProvider.validateRefreshToken(userId, refreshToken)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        RoleEnum role = RoleEnum.fromCode(user.getRole());
        return jwtTokenProvider.generateAccessToken(userId, role);
    }

    @Override
    public void logout(Long userId, String accessToken) {
        jwtTokenProvider.blacklistToken(accessToken);
        jwtTokenProvider.removeRefreshToken(userId);
    }
}
