package com.kuaid.service.impl;

import com.kuaid.dao.UserMapper;
import com.kuaid.dto.response.ErrorCode;
import com.kuaid.entity.User;
import com.kuaid.enums.RoleEnum;
import com.kuaid.exception.BusinessException;
import com.kuaid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        return user;
    }

    @Override
    public User updateProfile(Long userId, String nickname, String avatarUrl,
                              String phone, String realName, String studentId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        if (phone != null && !phone.equals(user.getPhone())) {
            User existing = userMapper.selectByPhone(phone);
            if (existing != null && !existing.getId().equals(userId)) {
                throw new BusinessException(ErrorCode.PHONE_ALREADY_BOUND);
            }
        }

        user.setNickname(nickname);
        user.setAvatarUrl(avatarUrl);
        user.setPhone(phone);
        user.setRealName(realName);
        user.setStudentId(studentId);
        userMapper.update(user);
        return user;
    }

    @Override
    public void applyForCourier(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        user.setRole(RoleEnum.COURIER.getCode());
        userMapper.update(user);
    }
}
