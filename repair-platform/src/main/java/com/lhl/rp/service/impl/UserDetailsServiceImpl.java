package com.lhl.rp.service.impl;

import com.lhl.rp.bean.LoginUser;
import com.lhl.rp.bean.TUser;
import com.lhl.rp.repository.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/26_22:27
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        TUser tUser = tUserMapper.selectByLoginAct(username);
        if (tUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new LoginUser(tUser);
    }
}
