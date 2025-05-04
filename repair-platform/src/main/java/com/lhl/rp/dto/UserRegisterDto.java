package com.lhl.rp.dto;

import lombok.Data;

/**
 * 用户注册表单
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/5/3_16:53
 */
@Data
public class UserRegisterDto {
    private String loginAct;
    private String loginPwd;
    private String confirmPwd;
    private String nickname;
    private String phone;
    private String email;
    private String avatarUrl;
    private String uuid;
    private String captcha;
}
