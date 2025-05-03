package com.lhl.rp.controller;

import com.lhl.rp.bean.TRole;
import com.lhl.rp.bean.TUser;
import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import com.lhl.rp.service.ProfileService;
import com.lhl.rp.service.TUserService;
import com.lhl.rp.service.exception.ProfileServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private ProfileService profileService;

    /**
     * 查询已登录用户信息
     */
    @PreAuthorize("hasAuthority('profile:me')")
    @GetMapping("/me")
    public R<?> me() {
        try {
            return R.ok(profileService.getCurrentUser());
        } catch (ProfileServiceException e) {
            return R.error(ResultCode.UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * 查询已登录用户角色
     */
    @PreAuthorize("hasAuthority('profile:role')")
    @GetMapping("/role")
    public R<?> role() {
        try {
            return R.ok(profileService.getCurrentUserRoles());
        } catch (ProfileServiceException e) {
            return R.error(ResultCode.UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * 查询已登录用户权限
     */
    @PreAuthorize("hasAuthority('profile:permission')")
    @GetMapping("/permission")
    public R<?> permission() {
        try {
            return R.ok(profileService.getCurrentUserPermissionCodes());
        } catch (ProfileServiceException e) {
            return R.error(ResultCode.UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * 查询已登录用户面板
     */
    @PreAuthorize("hasAuthority('profile:panel')")
    @GetMapping("/panel")
    public R<?> panel() {
        try {
            return R.ok(profileService.getCurrentUserPanels());
        } catch (ProfileServiceException e) {
            return R.error(ResultCode.UNAUTHORIZED, e.getMessage());
        }
    }
}
