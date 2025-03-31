package com.lhl.rp.service;

import com.lhl.rp.bean.TRole;

import java.util.List;

/**
 * 角色服务
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/31_12:51
 */
public interface TRoleService {

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    List<TRole> consultAllRole();
}
