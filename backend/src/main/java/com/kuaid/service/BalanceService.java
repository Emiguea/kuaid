package com.kuaid.service;

import com.kuaid.dto.response.PageResponse;
import com.kuaid.entity.BalanceRecord;

import java.math.BigDecimal;

public interface BalanceService {

    BigDecimal getBalance(Long userId);

    void recharge(Long userId, BigDecimal amount);

    void deduct(Long userId, BigDecimal amount, String orderNo, String description);

    void income(Long userId, BigDecimal amount, String orderNo, String description);

    void refund(Long userId, BigDecimal amount, String orderNo, String description);

    PageResponse<BalanceRecord> getRecords(Long userId, int page, int size);
}
