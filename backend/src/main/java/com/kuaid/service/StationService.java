package com.kuaid.service;

import com.kuaid.dto.request.StationCreateRequest;
import com.kuaid.entity.Station;

import java.util.List;

public interface StationService {

    Station create(StationCreateRequest request, Long managerId);

    Station update(Long id, StationCreateRequest request, Long managerId);

    Station getById(Long id);

    List<Station> listAll();

    List<Station> listByManager(Long managerId);
}
