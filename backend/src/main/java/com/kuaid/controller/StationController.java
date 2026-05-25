package com.kuaid.controller;

import com.kuaid.dto.request.StationCreateRequest;
import com.kuaid.dto.response.ApiResponse;
import com.kuaid.entity.Station;
import com.kuaid.security.SecurityUser;
import com.kuaid.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stations")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping
    public ApiResponse<List<Station>> list() {
        return ApiResponse.success(stationService.listAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Station> detail(@PathVariable Long id) {
        return ApiResponse.success(stationService.getById(id));
    }

    @PostMapping
    public ApiResponse<Station> create(@Validated @RequestBody StationCreateRequest request,
                                       @AuthenticationPrincipal SecurityUser user) {
        return ApiResponse.success(stationService.create(request, user.getUserId()));
    }

    @PutMapping("/{id}")
    public ApiResponse<Station> update(@PathVariable Long id,
                                       @Validated @RequestBody StationCreateRequest request,
                                       @AuthenticationPrincipal SecurityUser user) {
        return ApiResponse.success(stationService.update(id, request, user.getUserId()));
    }
}
