package com.lhl.rp.service.impl;

import com.lhl.rp.bean.TPermission;
import com.lhl.rp.bean.TRole;
import com.lhl.rp.repository.TRoleMapper;
import com.lhl.rp.service.TPermissionService;
import com.lhl.rp.service.TRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/31_12:57
 */
@Service
public class TRoleServiceImpl implements TRoleService {

    @Autowired
    private TRoleMapper tRoleMapper;

    @Autowired
    private TPermissionService tPermissionService;

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<TRole> consultAllRole() {
        return tRoleMapper.selectAll();
    }

    /**
     * 通过ID查询角色信息
     *
     * @param id 角色ID
     * @return 角色Bean
     */
    @Override
    public TRole consultById(Long id) {
        return tRoleMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过ID更新角色信息
     *
     * @param tRole 带ID信息的角色Bean
     * @return 成功记录条数
     */
    @Override
    public int updateById(TRole tRole) {
        int count = tRoleMapper.updateByPrimaryKeySelective(tRole);
        if (count != 1) throw new RuntimeException("更新失败");
        return count;
    }

    /**
     * 通过ID删除角色信息
     *
     * @param id 主键ID
     * @return 成功记录条数
     */
    @Override
    public int deleteById(Long id) {
        int count = tRoleMapper.deleteByPrimaryKey(id);
        if (count != 1) throw new RuntimeException("删除失败");
        return count;
    }

    /**
     * 通过ID集合批量删除角色信息
     *
     * @param ids 主键ID List集合
     * @return 成功记录条数
     */
    @Transactional
    @Override
    public int deleteByIds(List<Long> ids) {
        int count = tRoleMapper.deleteByPrimaryKeys(ids);
        if (count != ids.size()) throw new RuntimeException("部分条目删除失败，已回滚");
        return count;
    }

    /**
     * 根据角色标识符查询角色
     *
     * @param code 角色标识符
     * @return 角色Bean
     */
    @Override
    public TRole consultByCode(String code) {
        TRole tRole = tRoleMapper.selectByCode(code);
        if (tRole == null) {
            throw new RuntimeException("角色不存在");
        }
        return tRole;
    }

    /**
     * 创建角色
     *
     * @param tRole 角色Bean
     * @return 成功记录条数
     */
    @Transactional
    @Override
    public int creatRole(TRole tRole) {
        if (consultById(tRole.getId()) != null) {
            throw new RuntimeException("该条目已存在");
        }
        int count = tRoleMapper.insert(tRole);
        if (count != 1) throw new RuntimeException("新建角色失败");
        return count;
    }

    /**
     * 根据角色查询角色权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public List<TPermission> listPermission(Long roleId) {
        List<TPermission> tPermissions = tRoleMapper.listPermission(roleId);
        ArrayList<TPermission> list = new ArrayList<>();
        tPermissions.forEach(tPermission -> {
            if (tPermission != null && tPermission.getId() != null) {
                list.add(tPermission);
            }
        });
        return list;
    }

    /**
     * 根据角色查询角色API权限ID
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    @Override
    public List<Long> listPermissionIds(Long roleId) {
        ArrayList<Long> idList = new ArrayList<>();
        listPermission(roleId).forEach(tPermission -> {
            if ("api".equals(tPermission.getType())) {
                idList.add(tPermission.getId());
            }
        });
        return idList;
    }

    /**
     * 更新角色权限列表
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     * @return 更新记录条数
     */
    @Transactional
    @Override
    public int updateRolePermissions(Long roleId, List<Long> permissionIds) {
        TRole tRole = tRoleMapper.selectByPrimaryKey(roleId);
        if (tRole == null) throw new RuntimeException("角色不存在");
        if (permissionIds == null || permissionIds.isEmpty()) throw new RuntimeException("权限列表不能为空");
        // 1. 删除角色所有权限
        int c1 = tRoleMapper.deleteRolePermissions(roleId);
        // 2. 递归补全角色权限列表
        List<TPermission> allPermission = tPermissionService.consultAllPermission();
        Map<Long, TPermission> permissionMap = new HashMap<>();
        allPermission.forEach(tPermission -> {
            permissionMap.put(tPermission.getId(), tPermission);
        });
        Set<Long> completionIds = new HashSet<>();
        permissionIds.forEach(permissionId -> completionPermission(permissionMap, permissionId, completionIds));
        // 3. 添加角色权限
        int c2 = tRoleMapper.addRolePermissions(roleId, completionIds.stream().toList());
        // 4. 检查是否成功
        if (c1 != -1 && c2 == completionIds.size()) {
            return c2;
        }
        throw new RuntimeException("更新失败");
    }

    // 辅助方法：递归补全角色权限列表
    private void completionPermission(
            Map<Long, TPermission> permissionMap, // 全部权限哈希表，键为权限ID
            Long currentId, // 当前权限ID
            Set<Long> completionIds // 补全的权限ID列表

    ) {
        completionIds.add(currentId);
        if (permissionMap.get(currentId).getParentId() == 0) return;
        // 递归补全父级权限
        completionPermission(permissionMap, permissionMap.get(currentId).getParentId(), completionIds);
    }
}
