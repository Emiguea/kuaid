package com.kuaid.service;

import com.kuaid.dto.response.PageResponse;
import com.kuaid.entity.Express;
import com.kuaid.entity.Notification;
import com.kuaid.entity.Order;

public interface NotificationService {

    void notifyPackageArrival(Long userId, Express express);

    void notifyOrderStatusChange(Long userId, Order order, String message);

    PageResponse<Notification> listByUser(Long userId, int page, int size);

    int getUnreadCount(Long userId);

    void markAsRead(Long id, Long userId);

    void markAllAsRead(Long userId);
}
