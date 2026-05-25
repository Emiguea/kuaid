package com.kuaid.service;

import com.kuaid.dto.request.OrderCreateRequest;
import com.kuaid.dto.response.PageResponse;
import com.kuaid.entity.Order;

public interface OrderService {

    Order create(OrderCreateRequest request, Long studentId);

    Order accept(Long orderId, Long courierId);

    Order complete(Long orderId, Long courierId);

    Order cancel(Long orderId, Long userId, String reason);

    Order getById(Long id);

    PageResponse<Order> listByStudent(Long studentId, Integer status, int page, int size);

    PageResponse<Order> listByCourier(Long courierId, Integer status, int page, int size);

    PageResponse<Order> listPendingByStation(Long stationId, int page, int size);
}
