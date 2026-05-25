package com.kuaid.security;

import com.kuaid.dao.UserMapper;
import com.kuaid.entity.User;
import com.kuaid.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userMapper.selectById(Long.parseLong(userId));
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + userId);
        }
        RoleEnum role = RoleEnum.fromCode(user.getRole());
        return new SecurityUser(user.getId(), role.name());
    }
}
