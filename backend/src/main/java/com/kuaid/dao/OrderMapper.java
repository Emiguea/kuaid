package com.kuaid.dao;

import com.kuaid.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {

    Order selectById(@Param("id") Long id);

    Order selectByOrderNo(@Param("orderNo") String orderNo);

    List<Order> selectByStudentId(@Param("studentId") Long studentId,
                                  @Param("status") Integer status,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    long countByStudentId(@Param("studentId") Long studentId, @Param("status") Integer status);

    List<Order> selectByCourierId(@Param("courierId") Long courierId,
                                  @Param("status") Integer status,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    long countByCourierId(@Param("courierId") Long courierId, @Param("status") Integer status);

    List<Order> selectPendingByStationId(@Param("stationId") Long stationId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    long countPendingByStationId(@Param("stationId") Long stationId);

    int insert(Order order);

    int update(Order order);
}
