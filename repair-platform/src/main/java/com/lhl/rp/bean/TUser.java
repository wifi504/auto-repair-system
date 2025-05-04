package com.lhl.rp.bean;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * 用户表
 * t_user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 登录账号
     */
    private String loginAct;

    /**
     * 登录密码
     */
    @JsonIgnore
    private String loginPwd;

    /**
     * 真实姓名
     */
    private String realName;

    public String getRealName() {
        if (realName == null) return null;
        return realName.charAt(0) + "*".repeat(realName.length() - 2) + realName.charAt(realName.length() - 1);
    }

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 账号状态：0=禁用，1=启用
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;
}