package com.kuaid.controller;

import com.kuaid.dto.response.ApiResponse;
import com.kuaid.entity.User;
import com.kuaid.security.SecurityUser;
import com.kuaid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ApiResponse<User> getProfile(@AuthenticationPrincipal SecurityUser user) {
        User profile = userService.getProfile(user.getUserId());
        profile.setOpenid(null);
        profile.setUnionId(null);
        return ApiResponse.success(profile);
    }

    @PutMapping("/profile")
    public ApiResponse<User> updateProfile(@RequestBody Map<String, String> body,
                                           @AuthenticationPrincipal SecurityUser user) {
        User updated = userService.updateProfile(
                user.getUserId(),
                body.get("nickname"),
                body.get("avatarUrl"),
                body.get("phone"),
                body.get("realName"),
                body.get("studentId"));
        updated.setOpenid(null);
        updated.setUnionId(null);
        return ApiResponse.success(updated);
    }

    @PutMapping("/role")
    public ApiResponse<Void> applyForCourier(@AuthenticationPrincipal SecurityUser user) {
        userService.applyForCourier(user.getUserId());
        return ApiResponse.success();
    }
}
