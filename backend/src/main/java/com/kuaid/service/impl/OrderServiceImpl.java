package com.kuaid.service.impl;

import com.kuaid.dao.OrderMapper;
import com.kuaid.dto.request.OrderCreateRequest;
import com.kuaid.dto.response.ErrorCode;
import com.kuaid.dto.response.PageResponse;
import com.kuaid.entity.Order;
import com.kuaid.enums.OrderStatusEnum;
import com.kuaid.exception.BusinessException;
import com.kuaid.service.BalanceService;
import com.kuaid.service.NotificationService;
import com.kuaid.service.OrderService;
import com.kuaid.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional
    public Order create(OrderCreateRequest request, Long studentId) {
        String orderNo = generateOrderNo();

        balanceService.deduct(studentId, request.getFee(), orderNo, "代取快递服务费");

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setExpressId(request.getExpressId());
        order.setStudentId(studentId);
        order.setStationId(request.getStationId());
        order.setFee(request.getFee());
        order.setStatus(OrderStatusEnum.PENDING.getCode());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setStudentRemark(request.getStudentRemark());
        orderMapper.insert(order);
        return order;
    }

    @Override
    @Transactional
    public Order accept(Long orderId, Long courierId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        if (order.getStatus() != OrderStatusEnum.PENDING.getCode()) {
            throw new BusinessException(ErrorCode.ORDER_ALREADY_ACCEPTED);
        }

        order.setCourierId(courierId);
        order.setStatus(OrderStatusEnum.ACCEPTED.getCode());
        orderMapper.update(order);

        notificationService.notifyOrderStatusChange(order.getStudentId(), order, "您的订单已被接单");
        return order;
    }

    @Override
    @Transactional
    public Order complete(Long orderId, Long courierId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        if (!order.getCourierId().equals(courierId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN.getCode(), "无权操作此订单");
        }

        order.setStatus(OrderStatusEnum.COMPLETED.getCode());
        order.setCompletedAt(new Date());
        orderMapper.update(order);

        balanceService.income(courierId, order.getFee(), order.getOrderNo(), "代取快递收入");
        notificationService.notifyOrderStatusChange(order.getStudentId(), order, "您的快递已送达");
        return order;
    }

    @Override
    @Transactional
    public Order cancel(Long orderId, Long userId, String reason) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);

        if (order.getStatus() >= OrderStatusEnum.COMPLETED.getCode()) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_CANCEL);
        }

        order.setStatus(OrderStatusEnum.CANCELLED.getCode());
        order.setCancelledAt(new Date());
        order.setCancelReason(reason);
        orderMapper.update(order);

        balanceService.refund(order.getStudentId(), order.getFee(), order.getOrderNo(), "订单取消退款");

        if (order.getCourierId() != null) {
            notificationService.notifyOrderStatusChange(order.getCourierId(), order, "订单已被取消");
        }
        return order;
    }

    @Override
    public Order getById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        return order;
    }

    @Override
    public PageResponse<Order> listByStudent(Long studentId, Integer status, int page, int size) {
        int offset = (page - 1) * size;
        List<Order> list = orderMapper.selectByStudentId(studentId, status, offset, size);
        long total = orderMapper.countByStudentId(studentId, status);
        return new PageResponse<>(list, total, page, size);
    }

    @Override
    public PageResponse<Order> listByCourier(Long courierId, Integer status, int page, int size) {
        int offset = (page - 1) * size;
        List<Order> list = orderMapper.selectByCourierId(courierId, status, offset, size);
        long total = orderMapper.countByCourierId(courierId, status);
        return new PageResponse<>(list, total, page, size);
    }

    @Override
    public PageResponse<Order> listPendingByStation(Long stationId, int page, int size) {
        int offset = (page - 1) * size;
        List<Order> list = orderMapper.selectPendingByStationId(stationId, offset, size);
        long total = orderMapper.countPendingByStationId(stationId);
        return new PageResponse<>(list, total, page, size);
    }

    private String generateOrderNo() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = "order:seq:" + date;
        Long seq = redisUtil.increment(key);
        if (seq == 1L) {
            redisUtil.expire(key, 48, TimeUnit.HOURS);
        }
        return "KD" + date + String.format("%06d", seq);
    }
}
