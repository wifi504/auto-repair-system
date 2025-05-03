package com.lhl.rp.service;

import com.lhl.rp.bean.TPermission;
import com.lhl.rp.service.impl.TPermissionServiceImpl;

import java.util.List;

/**
 * 权限服务
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/30_14:20
 */
public interface TPermissionService {

    /**
     * 获取系统权限列表
     *
     * @return 权限列表
     */
    List<TPermission> consultAllPermission();

    /**
     * 获取系统权限列表（树形结构）
     *
     * @return 权限列表
     */
    List<TPermissionServiceImpl.PermissionNode> consultAllPermissionByTree();

    /**
     * 超管取得所有权限
     */
    void applyAllPermissionForSuperAdmin();
}
