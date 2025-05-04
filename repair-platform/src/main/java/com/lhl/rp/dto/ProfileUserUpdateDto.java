package com.lhl.rp.dto;

import lombok.Data;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/5/4_16:26
 */
@Data
public class ProfileUserUpdateDto {
    private Long id;
    private String loginAct;
    private String loginPwd;
    private String nickname;
    private String phone;
    private String email;
    private String avatarUrl;
}
