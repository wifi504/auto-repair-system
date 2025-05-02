package com.lhl.rp.service;

import com.lhl.rp.bean.TPermission;
import com.lhl.rp.bean.TRole;
import com.lhl.rp.service.exception.TRoleServiceException;

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
    int updateById(TRole tRole) throws TRoleServiceException;

    /**
     * 通过ID删除角色信息
     *
     * @param id 主键ID
     * @return 成功记录条数
     */
    int deleteById(Long id) throws TRoleServiceException;

    /**
     * 通过ID集合批量删除角色信息
     *
     * @param ids 主键ID List集合
     * @return 成功记录条数
     */
    int deleteByIds(List<Long> ids) throws TRoleServiceException;


    /**
     * 根据角色标识符查询角色
     *
     * @param code 角色标识符
     * @return 角色Bean
     */
    TRole consultByCode(String code) throws TRoleServiceException;

    /**
     * 创建角色
     *
     * @param tRole 角色Bean
     * @return 成功记录条数
     */
    int creatRole(TRole tRole) throws TRoleServiceException;

    /**
     * 根据角色查询角色权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<TPermission> listPermission(Long roleId);

    /**
     * 根据角色查询角色API权限ID
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> listPermissionIds(Long roleId);

    /**
     * 更新角色权限列表
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     * @return 更新记录条数
     */
    int updateRolePermissions(Long roleId, List<Long> permissionIds) throws TRoleServiceException;
}
