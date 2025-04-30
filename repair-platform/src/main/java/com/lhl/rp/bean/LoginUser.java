package com.lhl.rp.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/26_22:23
 */

@Data
@AllArgsConstructor
public class LoginUser implements UserDetails {

    // 逆向工程实体类
    private TUser tUser;

    // 用户的权限标识符列表
    private List<String> permissionCodes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissionCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return tUser.getLoginPwd(); // 数据库字段
    }

    @Override
    public String getUsername() {
        return tUser.getLoginAct(); // 数据库字段
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
