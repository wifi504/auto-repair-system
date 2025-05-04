package com.lhl.rp.dto;

import lombok.Data;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/5/4_20:39
 */
@Data
public class ProfileUpdatePwdDto {
    private Long id;
    private String oldPwd;
    private String loginPwd;
    private String confirmNewPwd;
}
