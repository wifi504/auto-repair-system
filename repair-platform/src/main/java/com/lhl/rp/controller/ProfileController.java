package com.lhl.rp.controller;

import com.lhl.rp.bean.LoginUser;
import com.lhl.rp.bean.TRole;
import com.lhl.rp.bean.TUser;
import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import com.lhl.rp.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;

/**
 * 个人管理
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/5/1_12:40
 */
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private TUserService tUserService;

    /**
     * 查询已登录用户信息
     */
    @PreAuthorize("hasAuthority('profile:me')")
    @GetMapping("/me")
    public R<?> me() {
        TUser currentUser = getCurrentUser();
        if (currentUser == null) {
            return R.error(ResultCode.UNAUTHORIZED);
        }
        return R.ok(currentUser);
    }

    /**
     * 查询已登录用户角色
     */
    @PreAuthorize("hasAuthority('profile:role')")
    @GetMapping("/role")
    public R<?> role() {
        TUser currentUser = getCurrentUser();
        if (currentUser == null) {
            return R.error(ResultCode.UNAUTHORIZED);
        }
        List<TRole> tRoles = tUserService.consultAllRolesByUserId(currentUser.getId());
        return R.ok(tRoles);
    }

    /**
     * 查询已登录用户权限
     */
    @PreAuthorize("hasAuthority('profile:permission')")
    @GetMapping("/permission")
    public R<?> permission() {
        TUser currentUser = getCurrentUser();
        if (currentUser == null) {
            return R.error(ResultCode.UNAUTHORIZED);
        }
        List<String> codes = tUserService.queryPermissionCodes(currentUser.getId());
        return R.ok(codes);
    }

    /**
     * 查询已登录用户面板
     */
    @PreAuthorize("hasAuthority('profile:panel')")
    @GetMapping("/panel")
    public R<?> panel() {
        TUser currentUser = getCurrentUser();
        if (currentUser == null) {
            return R.error(ResultCode.UNAUTHORIZED);
        }
        List<TRole> tRoles = tUserService.consultAllRolesByUserId(currentUser.getId());
        HashSet<String> panels = new HashSet<>();
        tRoles.forEach(tRole -> {
            panels.add(tRole.getCode().split("_")[0]);
        });
        return R.ok(panels);
    }

    // 辅助方法：获取当前用户
    private TUser getCurrentUser() {
        // 从 Spring Security 上下文获取当前认证用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser loginUser)) {
            return null;
        }
        return loginUser.getTUser();
    }
}
