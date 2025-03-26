package com.lhl.rp.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/26_22:23
 */

@Data
@AllArgsConstructor
public class LoginUser implements UserDetails {

    /**
     * 逆向工程实体类
     */
    private TUser tUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // TODO 增加权限
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
