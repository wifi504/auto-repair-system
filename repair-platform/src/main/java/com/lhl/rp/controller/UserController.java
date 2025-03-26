package com.lhl.rp.controller;

import com.lhl.rp.bean.LoginUser;
import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息接口
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/26_23:59
 */

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public R<?> getCurrentUser() {
        // 从 Spring Security 上下文获取当前认证用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser loginUser)) {
            return R.error(ResultCode.UNAUTHORIZED);
        }

        return R.ok(loginUser.getTUser());
    }
}
