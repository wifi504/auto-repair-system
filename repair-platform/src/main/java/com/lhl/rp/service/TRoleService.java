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

    /**
     * 通过ID查询角色信息
     *
     * @param id 角色ID
     * @return 角色Bean
     */
    TRole consultById(Long id);

    /**
     * 通过ID更新角色信息
     *
     * @param tRole 携带id信息的角色Bean
     * @return 成功记录条数
     */
    int updateById(TRole tRole);

    /**
     * 通过ID删除角色信息
     *
     * @param id 主键ID
     * @return 成功记录条数
     */
    int deleteById(Long id);

    /**
     * 通过ID集合批量删除角色信息
     *
     * @param ids 主键ID List集合
     * @return 成功记录条数
     */
    int deleteByIds(List<Long> ids);

    /**
     * 创建角色
     *
     * @param tRole 角色Bean
     * @return 成功记录条数
     */
    int creatRole(TRole tRole);
}
