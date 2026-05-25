package com.kuaid.controller;

import com.kuaid.dto.request.OrderCreateRequest;
import com.kuaid.dto.response.ApiResponse;
import com.kuaid.dto.response.PageResponse;
import com.kuaid.entity.Order;
import com.kuaid.security.SecurityUser;
import com.kuaid.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ApiResponse<Order> create(@Validated @RequestBody OrderCreateRequest request,
                                     @AuthenticationPrincipal SecurityUser user) {
        return ApiResponse.success(orderService.create(request, user.getUserId()));
    }

    @GetMapping
    public ApiResponse<PageResponse<Order>> list(
            @RequestParam String role,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal SecurityUser user) {
        if ("courier".equals(role)) {
            return ApiResponse.success(orderService.listByCourier(user.getUserId(), status, page, size));
        }
        return ApiResponse.success(orderService.listByStudent(user.getUserId(), status, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<Order> detail(@PathVariable Long id) {
        return ApiResponse.success(orderService.getById(id));
    }

    @PutMapping("/{id}/accept")
    public ApiResponse<Order> accept(@PathVariable Long id,
                                     @AuthenticationPrincipal SecurityUser user) {
        return ApiResponse.success(orderService.accept(id, user.getUserId()));
    }

    @PutMapping("/{id}/complete")
    public ApiResponse<Order> complete(@PathVariable Long id,
                                       @AuthenticationPrincipal SecurityUser user) {
        return ApiResponse.success(orderService.complete(id, user.getUserId()));
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<Order> cancel(@PathVariable Long id,
                                     @RequestBody Map<String, String> body,
                                     @AuthenticationPrincipal SecurityUser user) {
        return ApiResponse.success(orderService.cancel(id, user.getUserId(), body.get("reason")));
    }
}
