package com.kuaid.dao;

import com.kuaid.entity.BalanceRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BalanceMapper {

    List<BalanceRecord> selectByUserId(@Param("userId") Long userId,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    long countByUserId(@Param("userId") Long userId);

    int insert(BalanceRecord record);
}
