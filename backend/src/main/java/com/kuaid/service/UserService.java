package com.kuaid.service;

import com.kuaid.entity.User;

public interface UserService {

    User getProfile(Long userId);

    User updateProfile(Long userId, String nickname, String avatarUrl, String phone, String realName, String studentId);

    void applyForCourier(Long userId);
}
