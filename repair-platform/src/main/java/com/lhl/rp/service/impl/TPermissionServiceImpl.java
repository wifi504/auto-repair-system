package com.lhl.rp.service.impl;

import com.lhl.rp.bean.TPermission;
import com.lhl.rp.repository.TPermissionMapper;
import com.lhl.rp.service.TPermissionService;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/30_14:22
 */
@Service
public class TPermissionServiceImpl implements TPermissionService {

    @Autowired
    private TPermissionMapper tPermissionMapper;

    /**
     * 获取系统权限列表
     *
     * @return 权限列表
     */
    @Override
    public List<TPermission> consultAllPermission() {
        return tPermissionMapper.consultAllPermission();
    }

    /**
     * 获取系统权限列表（树形结构）
     *
     * @return 权限列表
     */
    @Override
    public List<PermissionNode> consultAllPermissionByTree() {
        List<TPermission> tPermissions = tPermissionMapper.consultAllPermission();
        // 1. 获取所有一级权限
        ArrayList<PermissionNode> roots = new ArrayList<>();
        tPermissions.forEach(tPermission -> {
            if (tPermission.getParentId() == 0) {
                roots.add(PermissionNode.builder()
                        .value(tPermission.getId())
                        .label(tPermission.getName())
                        .build());
            }
        });
        // 2. 为每个节点构建子树
        roots.forEach(root -> {
            buildChildren(root, tPermissions);
        });
        return roots;
    }

    /**
     * 超管取得所有权限
     */
    @Override
    public void consultAllPermissionBySuperAdmin() {
        tPermissionMapper.consultAllPermissionBySuperAdmin();
    }

    @Data
    @Builder
    public static class PermissionNode {
        private Long value; // 权限ID
        private String label; // 权限名称
        private List<PermissionNode> children;
    }

    // 辅助方法：给目标节点递归构造子树
    private void buildChildren(PermissionNode targetNode, List<TPermission> permissions) {
        // 1. 获取当前节点的子节点列表
        ArrayList<PermissionNode> nodes = new ArrayList<>();
        permissions.forEach(tPermission -> {
            if (targetNode.getValue().equals(tPermission.getParentId())) {
                nodes.add(PermissionNode.builder()
                        .value(tPermission.getId())
                        .label(tPermission.getName())
                        .build());
            }
        });
        // 2. 为当前节点设置子树
        targetNode.setChildren(nodes);
        // 3. 为每个子节点构建子树
        nodes.forEach(node -> buildChildren(node, permissions));
    }
}
