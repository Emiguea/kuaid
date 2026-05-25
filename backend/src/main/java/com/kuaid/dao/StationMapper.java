package com.kuaid.dao;

import com.kuaid.entity.Station;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StationMapper {

    Station selectById(@Param("id") Long id);

    List<Station> selectAll();

    List<Station> selectByManagerId(@Param("managerId") Long managerId);

    int insert(Station station);

    int update(Station station);
}
