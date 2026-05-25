package com.kuaid.service.impl;

import com.kuaid.dao.StationMapper;
import com.kuaid.dto.request.StationCreateRequest;
import com.kuaid.dto.response.ErrorCode;
import com.kuaid.entity.Station;
import com.kuaid.exception.BusinessException;
import com.kuaid.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationMapper stationMapper;

    @Override
    public Station create(StationCreateRequest request, Long managerId) {
        Station station = new Station();
        station.setName(request.getName());
        station.setAddress(request.getAddress());
        if (request.getLongitude() != null) {
            station.setLongitude(BigDecimal.valueOf(request.getLongitude()));
        }
        if (request.getLatitude() != null) {
            station.setLatitude(BigDecimal.valueOf(request.getLatitude()));
        }
        station.setContactPhone(request.getContactPhone());
        station.setManagerId(managerId);
        station.setStatus(1);
        stationMapper.insert(station);
        return station;
    }

    @Override
    public Station update(Long id, StationCreateRequest request, Long managerId) {
        Station station = stationMapper.selectById(id);
        if (station == null) {
            throw new BusinessException(ErrorCode.STATION_NOT_FOUND);
        }
        if (!station.getManagerId().equals(managerId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN.getCode(), "无权操作此站点");
        }

        station.setName(request.getName());
        station.setAddress(request.getAddress());
        if (request.getLongitude() != null) {
            station.setLongitude(BigDecimal.valueOf(request.getLongitude()));
        }
        if (request.getLatitude() != null) {
            station.setLatitude(BigDecimal.valueOf(request.getLatitude()));
        }
        station.setContactPhone(request.getContactPhone());
        stationMapper.update(station);
        return station;
    }

    @Override
    public Station getById(Long id) {
        Station station = stationMapper.selectById(id);
        if (station == null) {
            throw new BusinessException(ErrorCode.STATION_NOT_FOUND);
        }
        return station;
    }

    @Override
    public List<Station> listAll() {
        return stationMapper.selectAll();
    }

    @Override
    public List<Station> listByManager(Long managerId) {
        return stationMapper.selectByManagerId(managerId);
    }
}
