package com.kuaid.service.impl;

import com.kuaid.dao.ExpressMapper;
import com.kuaid.dao.UserMapper;
import com.kuaid.dto.request.ExpressRegisterRequest;
import com.kuaid.dto.response.ErrorCode;
import com.kuaid.dto.response.PageResponse;
import com.kuaid.entity.Express;
import com.kuaid.entity.User;
import com.kuaid.enums.ExpressStatusEnum;
import com.kuaid.exception.BusinessException;
import com.kuaid.service.ExpressService;
import com.kuaid.service.NotificationService;
import com.kuaid.util.PickupCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ExpressServiceImpl implements ExpressService {

    @Autowired
    private ExpressMapper expressMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PickupCodeGenerator pickupCodeGenerator;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public Express register(ExpressRegisterRequest request, Long courierId) {
        if (expressMapper.existsByTrackingNoAndStation(request.getTrackingNo(), request.getStationId()) > 0) {
            throw new BusinessException(ErrorCode.TRACKING_NO_DUPLICATE);
        }

        String pickupCode = pickupCodeGenerator.generate(request.getStationId());

        Express express = new Express();
        express.setTrackingNo(request.getTrackingNo());
        express.setCompany(request.getCompany());
        express.setStationId(request.getStationId());
        express.setRecipientPhone(request.getRecipientPhone());
        express.setRecipientName(request.getRecipientName());
        express.setPickupCode(pickupCode);
        express.setShelfNo(request.getShelfNo());
        express.setStatus(ExpressStatusEnum.PENDING_PICKUP.getCode());
        express.setRegisteredBy(courierId);
        express.setRemark(request.getRemark());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        express.setExpireAt(cal.getTime());

        expressMapper.insert(express);

        User recipient = userMapper.selectByPhone(request.getRecipientPhone());
        if (recipient != null) {
            notificationService.notifyPackageArrival(recipient.getId(), express);
        }

        return express;
    }

    @Override
    public PageResponse<Express> listByStation(Long stationId, Integer status, int page, int size) {
        int offset = (page - 1) * size;
        List<Express> list = expressMapper.selectByStationAndStatus(stationId, status, offset, size);
        long total = expressMapper.countByStationAndStatus(stationId, status);
        return new PageResponse<>(list, total, page, size);
    }

    @Override
    public PageResponse<Express> listByRecipientPhone(String phone, int page, int size) {
        int offset = (page - 1) * size;
        List<Express> list = expressMapper.selectByRecipientPhone(phone, offset, size);
        long total = expressMapper.countByRecipientPhone(phone);
        return new PageResponse<>(list, total, page, size);
    }

    @Override
    public Express getById(Long id) {
        Express express = expressMapper.selectById(id);
        if (express == null) {
            throw new BusinessException(ErrorCode.EXPRESS_NOT_FOUND);
        }
        return express;
    }

    @Override
    @Transactional
    public void pickup(Long expressId, String pickupCode, Long userId) {
        Express express = expressMapper.selectById(expressId);
        if (express == null) {
            throw new BusinessException(ErrorCode.EXPRESS_NOT_FOUND);
        }
        if (express.getStatus() != ExpressStatusEnum.PENDING_PICKUP.getCode()) {
            throw new BusinessException(ErrorCode.EXPRESS_ALREADY_PICKED);
        }
        if (!express.getPickupCode().equals(pickupCode)) {
            throw new BusinessException(ErrorCode.PICKUP_CODE_INVALID);
        }
        if (express.getExpireAt() != null && express.getExpireAt().before(new Date())) {
            throw new BusinessException(ErrorCode.EXPRESS_EXPIRED);
        }

        express.setStatus(ExpressStatusEnum.PICKED_UP.getCode());
        express.setPickedBy(userId);
        express.setPickedAt(new Date());
        expressMapper.update(express);
    }
}
