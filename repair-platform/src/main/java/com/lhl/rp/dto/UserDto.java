package com.lhl.rp.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/1_23:03
 */
@Data
public class UserDto {

    private Long id;
    private String loginAct;
    private String loginPwd;
    private String realName;
    private String nickname;
    private String phone;
    private String email;
    private String avatarUrl;
    private Byte status;
    private Date createTime;

    public String getLoginAct() {
        return loginAct == null ? null : loginAct.trim();
    }

    public String getLoginPwd() {
        return loginPwd == null ? null : loginPwd.trim();
    }

    public String getRealName() {
        return realName == null ? null : realName.trim();
    }

    public String getNickname() {
        return nickname == null ? null : nickname.trim();
    }

    public String getPhone() {
        return phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email == null ? null : email.trim();
    }

    public String getAvatarUrl() {
        return avatarUrl == null ? null : avatarUrl.trim();
    }
}
