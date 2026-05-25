package com.kuaid.service;

import com.kuaid.dto.request.ExpressRegisterRequest;
import com.kuaid.dto.response.PageResponse;
import com.kuaid.entity.Express;

public interface ExpressService {

    Express register(ExpressRegisterRequest request, Long courierId);

    PageResponse<Express> listByStation(Long stationId, Integer status, int page, int size);

    PageResponse<Express> listByRecipientPhone(String phone, int page, int size);

    Express getById(Long id);

    void pickup(Long expressId, String pickupCode, Long userId);
}
