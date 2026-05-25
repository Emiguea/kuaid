package com.kuaid.service.impl;

import com.kuaid.dao.NotificationMapper;
import com.kuaid.dto.response.PageResponse;
import com.kuaid.entity.Express;
import com.kuaid.entity.Notification;
import com.kuaid.entity.Order;
import com.kuaid.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public void notifyPackageArrival(Long userId, Express express) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(0);
        notification.setTitle("您有新的快递到达");
        notification.setContent(String.format("您的快递(%s)已到达，取件码：%s，请及时取件",
                express.getCompany() != null ? express.getCompany() : "快递",
                express.getPickupCode()));
        notification.setIsRead(0);
        notification.setExtraData("{\"expressId\":" + express.getId() + "}");
        notificationMapper.insert(notification);

        // TODO: 发送微信模板消息
        log.info("Package arrival notification sent to user {}", userId);
    }

    @Override
    public void notifyOrderStatusChange(Long userId, Order order, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(1);
        notification.setTitle("订单状态更新");
        notification.setContent(String.format("订单%s：%s", order.getOrderNo(), message));
        notification.setIsRead(0);
        notification.setExtraData("{\"orderId\":" + order.getId() + "}");
        notificationMapper.insert(notification);

        log.info("Order status notification sent to user {}", userId);
    }

    @Override
    public PageResponse<Notification> listByUser(Long userId, int page, int size) {
        int offset = (page - 1) * size;
        List<Notification> list = notificationMapper.selectByUserId(userId, offset, size);
        long total = notificationMapper.countByUserId(userId);
        return new PageResponse<>(list, total, page, size);
    }

    @Override
    public int getUnreadCount(Long userId) {
        return notificationMapper.countUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(Long id, Long userId) {
        notificationMapper.markAsRead(id, userId);
    }

    @Override
    public void markAllAsRead(Long userId) {
        notificationMapper.markAllAsRead(userId);
    }
}
