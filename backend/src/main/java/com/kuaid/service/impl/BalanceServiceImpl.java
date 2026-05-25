package com.kuaid.service.impl;

import com.kuaid.dao.BalanceMapper;
import com.kuaid.dao.UserMapper;
import com.kuaid.dto.response.ErrorCode;
import com.kuaid.dto.response.PageResponse;
import com.kuaid.entity.BalanceRecord;
import com.kuaid.entity.User;
import com.kuaid.enums.BalanceTypeEnum;
import com.kuaid.exception.BusinessException;
import com.kuaid.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    @Override
    public BigDecimal getBalance(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        return user.getBalance();
    }

    @Override
    @Transactional
    public void recharge(Long userId, BigDecimal amount) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        BigDecimal before = user.getBalance();
        BigDecimal after = before.add(amount);
        userMapper.updateBalance(userId, after);

        BalanceRecord record = new BalanceRecord();
        record.setUserId(userId);
        record.setType(BalanceTypeEnum.RECHARGE.getCode());
        record.setAmount(amount);
        record.setBeforeBalance(before);
        record.setAfterBalance(after);
        record.setDescription("账户充值");
        balanceMapper.insert(record);
    }

    @Override
    @Transactional
    public void deduct(Long userId, BigDecimal amount, String orderNo, String description) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        BigDecimal before = user.getBalance();
        if (before.compareTo(amount) < 0) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        BigDecimal after = before.subtract(amount);
        userMapper.updateBalance(userId, after);

        BalanceRecord record = new BalanceRecord();
        record.setUserId(userId);
        record.setType(BalanceTypeEnum.PAYMENT.getCode());
        record.setAmount(amount.negate());
        record.setBeforeBalance(before);
        record.setAfterBalance(after);
        record.setRelatedOrderNo(orderNo);
        record.setDescription(description);
        balanceMapper.insert(record);
    }

    @Override
    @Transactional
    public void income(Long userId, BigDecimal amount, String orderNo, String description) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        BigDecimal before = user.getBalance();
        BigDecimal after = before.add(amount);
        userMapper.updateBalance(userId, after);

        BalanceRecord record = new BalanceRecord();
        record.setUserId(userId);
        record.setType(BalanceTypeEnum.INCOME.getCode());
        record.setAmount(amount);
        record.setBeforeBalance(before);
        record.setAfterBalance(after);
        record.setRelatedOrderNo(orderNo);
        record.setDescription(description);
        balanceMapper.insert(record);
    }

    @Override
    @Transactional
    public void refund(Long userId, BigDecimal amount, String orderNo, String description) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        BigDecimal before = user.getBalance();
        BigDecimal after = before.add(amount);
        userMapper.updateBalance(userId, after);

        BalanceRecord record = new BalanceRecord();
        record.setUserId(userId);
        record.setType(BalanceTypeEnum.REFUND.getCode());
        record.setAmount(amount);
        record.setBeforeBalance(before);
        record.setAfterBalance(after);
        record.setRelatedOrderNo(orderNo);
        record.setDescription(description);
        balanceMapper.insert(record);
    }

    @Override
    public PageResponse<BalanceRecord> getRecords(Long userId, int page, int size) {
        int offset = (page - 1) * size;
        List<BalanceRecord> list = balanceMapper.selectByUserId(userId, offset, size);
        long total = balanceMapper.countByUserId(userId);
        return new PageResponse<>(list, total, page, size);
    }
}
