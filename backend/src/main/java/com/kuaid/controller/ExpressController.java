package com.kuaid.controller;

import com.kuaid.dto.request.ExpressRegisterRequest;
import com.kuaid.dto.response.ApiResponse;
import com.kuaid.dto.response.PageResponse;
import com.kuaid.entity.Express;
import com.kuaid.entity.User;
import com.kuaid.dao.UserMapper;
import com.kuaid.security.SecurityUser;
import com.kuaid.service.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/express")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping
    public ApiResponse<Express> register(@Validated @RequestBody ExpressRegisterRequest request,
                                         @AuthenticationPrincipal SecurityUser user) {
        Express express = expressService.register(request, user.getUserId());
        return ApiResponse.success(express);
    }

    @GetMapping
    public ApiResponse<PageResponse<Express>> listByStation(
            @RequestParam Long stationId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(expressService.listByStation(stationId, status, page, size));
    }

    @GetMapping("/my")
    public ApiResponse<PageResponse<Express>> myExpress(
            @AuthenticationPrincipal SecurityUser user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        User u = userMapper.selectById(user.getUserId());
        return ApiResponse.success(expressService.listByRecipientPhone(u.getPhone(), page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<Express> detail(@PathVariable Long id) {
        return ApiResponse.success(expressService.getById(id));
    }

    @PutMapping("/{id}/pickup")
    public ApiResponse<Void> pickup(@PathVariable Long id,
                                    @RequestBody Map<String, String> body,
                                    @AuthenticationPrincipal SecurityUser user) {
        expressService.pickup(id, body.get("pickupCode"), user.getUserId());
        return ApiResponse.success();
    }
}
