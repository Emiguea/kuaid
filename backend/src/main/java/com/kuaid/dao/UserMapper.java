package com.kuaid.dao;

import com.kuaid.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    User selectById(@Param("id") Long id);

    User selectByOpenid(@Param("openid") String openid);

    User selectByPhone(@Param("phone") String phone);

    int insert(User user);

    int update(User user);

    int updateBalance(@Param("id") Long id, @Param("balance") java.math.BigDecimal balance);
}
