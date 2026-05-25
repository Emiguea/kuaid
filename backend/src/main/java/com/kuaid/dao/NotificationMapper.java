package com.kuaid.dao;

import com.kuaid.entity.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NotificationMapper {

    List<Notification> selectByUserId(@Param("userId") Long userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    long countByUserId(@Param("userId") Long userId);

    int countUnreadByUserId(@Param("userId") Long userId);

    int insert(Notification notification);

    int markAsRead(@Param("id") Long id, @Param("userId") Long userId);

    int markAllAsRead(@Param("userId") Long userId);
}
