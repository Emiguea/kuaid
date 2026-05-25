package com.kuaid.controller;

import com.kuaid.dto.response.ApiResponse;
import com.kuaid.dto.response.PageResponse;
import com.kuaid.entity.Notification;
import com.kuaid.security.SecurityUser;
import com.kuaid.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ApiResponse<PageResponse<Notification>> list(
            @AuthenticationPrincipal SecurityUser user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(notificationService.listByUser(user.getUserId(), page, size));
    }

    @GetMapping("/unread-count")
    public ApiResponse<Map<String, Integer>> unreadCount(@AuthenticationPrincipal SecurityUser user) {
        int count = notificationService.getUnreadCount(user.getUserId());
        return ApiResponse.success(Map.of("count", count));
    }

    @PutMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(@PathVariable Long id,
                                        @AuthenticationPrincipal SecurityUser user) {
        notificationService.markAsRead(id, user.getUserId());
        return ApiResponse.success();
    }

    @PutMapping("/read-all")
    public ApiResponse<Void> markAllAsRead(@AuthenticationPrincipal SecurityUser user) {
        notificationService.markAllAsRead(user.getUserId());
        return ApiResponse.success();
    }
}
