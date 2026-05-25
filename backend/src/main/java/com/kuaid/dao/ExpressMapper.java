package com.kuaid.dao;

import com.kuaid.entity.Express;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExpressMapper {

    Express selectById(@Param("id") Long id);

    List<Express> selectByStationAndStatus(@Param("stationId") Long stationId,
                                           @Param("status") Integer status,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);

    long countByStationAndStatus(@Param("stationId") Long stationId, @Param("status") Integer status);

    List<Express> selectByRecipientPhone(@Param("recipientPhone") String recipientPhone,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    long countByRecipientPhone(@Param("recipientPhone") String recipientPhone);

    Express selectByPickupCodeAndStation(@Param("pickupCode") String pickupCode,
                                         @Param("stationId") Long stationId);

    int insert(Express express);

    int update(Express express);

    int existsByTrackingNoAndStation(@Param("trackingNo") String trackingNo, @Param("stationId") Long stationId);
}
