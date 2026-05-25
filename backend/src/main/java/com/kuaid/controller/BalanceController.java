package com.kuaid.controller;

import com.kuaid.dto.request.RechargeRequest;
import com.kuaid.dto.response.ApiResponse;
import com.kuaid.dto.response.PageResponse;
import com.kuaid.entity.BalanceRecord;
import com.kuaid.security.SecurityUser;
import com.kuaid.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping
    public ApiResponse<Map<String, BigDecimal>> getBalance(@AuthenticationPrincipal SecurityUser user) {
        BigDecimal balance = balanceService.getBalance(user.getUserId());
        return ApiResponse.success(Map.of("balance", balance));
    }

    @GetMapping("/records")
    public ApiResponse<PageResponse<BalanceRecord>> getRecords(
            @AuthenticationPrincipal SecurityUser user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(balanceService.getRecords(user.getUserId(), page, size));
    }

    @PostMapping("/recharge")
    public ApiResponse<Void> recharge(@Validated @RequestBody RechargeRequest request,
                                      @AuthenticationPrincipal SecurityUser user) {
        balanceService.recharge(user.getUserId(), request.getAmount());
        return ApiResponse.success();
    }
}
