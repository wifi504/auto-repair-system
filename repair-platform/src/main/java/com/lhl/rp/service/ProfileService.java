package com.lhl.rp.service;

import com.lhl.rp.bean.TRole;
import com.lhl.rp.bean.TUser;
import com.lhl.rp.dto.ProfileUpdatePwdDto;
import com.lhl.rp.dto.ProfileUserUpdateDto;
import com.lhl.rp.service.exception.ProfileServiceException;
import com.lhl.rp.service.impl.ProfileServiceImpl;

import java.util.List;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/5/2_20:01
 */
public interface ProfileService {

    /**
     * 获取当前用户信息
     */
    TUser getCurrentUser() throws ProfileServiceException;

    /**
     * 获取当前用户角色
     */
    List<TRole> getCurrentUserRoles() throws ProfileServiceException;

    /**
     * 获取当前用户权限标识符列表
     */
    List<String> getCurrentUserPermissionCodes() throws ProfileServiceException;

    /**
     * 获取当前用户面板列表
     */
    List<?> getCurrentUserPanels() throws ProfileServiceException;

    /**
     * 修改当前用户信息
     */
    void updateeCurrentUser(ProfileUserUpdateDto dto) throws ProfileServiceException;

    /**
     * 修改当前用户密码
     */
    void editCurrentUserPwd(ProfileUpdatePwdDto dto) throws ProfileServiceException;
}
