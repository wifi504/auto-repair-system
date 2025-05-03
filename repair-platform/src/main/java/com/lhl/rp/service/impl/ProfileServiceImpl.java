package com.lhl.rp.service.impl;

import com.lhl.rp.bean.LoginUser;
import com.lhl.rp.bean.TPermission;
import com.lhl.rp.bean.TRole;
import com.lhl.rp.bean.TUser;
import com.lhl.rp.service.ProfileService;
import com.lhl.rp.service.TPermissionService;
import com.lhl.rp.service.TUserService;
import com.lhl.rp.service.exception.ProfileServiceException;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/5/2_20:09
 */
@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    TUserService tUserService;

    @Autowired
    TPermissionService tPermissionService;

    /**
     * 获取当前用户信息
     */
    @Override
    public TUser getCurrentUser() throws ProfileServiceException {
        // 从 Spring Security 上下文获取当前认证用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser loginUser)) {
            throw new ProfileServiceException("无效认证信息");
        }
        return loginUser.getTUser();
    }

    /**
     * 获取当前用户角色
     */
    @Override
    public List<TRole> getCurrentUserRoles() throws ProfileServiceException {
        return tUserService.consultAllRolesByUserId(getCurrentUser().getId());
    }

    /**
     * 获取当前用户权限标识符列表
     */
    @Override
    public List<String> getCurrentUserPermissionCodes() throws ProfileServiceException {
        return tUserService.queryPermissionCodes(getCurrentUser().getId());
    }

    /**
     * 获取当前用户面板列表
     */
    @Override
    public List<Panel> getCurrentUserPanels() throws ProfileServiceException {
        try {
            // 获取所有权限
            List<TPermission> tPermissions = tPermissionService.consultAllPermission();
            List<TPermission> listPermissions = tPermissions.stream()
                    .filter(tPermission -> "list".equals(tPermission.getType())).toList();
            List<TPermission> menuPermissions = tPermissions.stream()
                    .filter(tPermission -> "menu".equals(tPermission.getType())).toList();
            // 构建所有的菜单列表
            List<MenuNode> commonMenus = new ArrayList<>();
            List<MenuNode> platformMenus = new ArrayList<>();
            List<MenuNode> merchantMenus = new ArrayList<>();
            List<MenuNode> customerMenus = new ArrayList<>();
            listPermissions.forEach(tPermission -> {
                switch (tPermission.getUrl()) {
                    case "COMMON" -> commonMenus.add(buildMenuNodeList(tPermission, menuPermissions));
                    case "PLATFORM" -> platformMenus.add(buildMenuNodeList(tPermission, menuPermissions));
                    case "MERCHANT" -> merchantMenus.add(buildMenuNodeList(tPermission, menuPermissions));
                    case "CUSTOMER" -> customerMenus.add(buildMenuNodeList(tPermission, menuPermissions));
                    default -> throw new RuntimeException("菜单类型无效：" + tPermission.getName());
                }
            });
            List<MenuNode> platformAllMenus = new ArrayList<>() {{
                addAll(commonMenus);
                addAll(platformMenus);
            }};
            List<MenuNode> merchantAllMenus = new ArrayList<>() {{
                addAll(commonMenus);
                addAll(merchantMenus);
            }};
            List<MenuNode> customerAllMenus = new ArrayList<>() {{
                addAll(commonMenus);
                addAll(customerMenus);
            }};

            // 通过用户角色统计用户具有的面板名
            HashSet<String> panelNames = new HashSet<>();
            List<TRole> roles = getCurrentUserRoles();
            roles.forEach(role -> panelNames.add(role.getCode().split("_")[0]));
            // 根据面板名构建面板列表
            List<Panel> panels = new ArrayList<>();
            panelNames.forEach(panelName -> panels.add(Panel.builder()
                    .name(panelName)
                    .menus(switch (panelName) {
                        case "PLATFORM" -> platformAllMenus;
                        case "MERCHANT" -> merchantAllMenus;
                        case "CUSTOMER" -> customerAllMenus;
                        default -> throw new RuntimeException("面板类型无效：" + panelName);
                    })
                    .build()));
            return panels;
        } catch (Exception e) {
            throw new ProfileServiceException(e.getMessage());
        }
    }

    // 面板（名称 + 菜单列表）
    @Data
    @Builder
    public static class Panel {
        private String name;
        private List<MenuNode> menus;
    }

    // 菜单数据节点
    // {index, title, icon, children, disabled}
    @Data
    @Builder
    public static class MenuNode {
        private String index;
        private String title;
        private String icon;
        private List<MenuNode> children;
        private Boolean disabled;
    }

    /**
     * 辅助方法
     * <hr/>
     * 根据父级权限，获取包含子代的 MenuNode
     *
     * @param parentPermission List权限
     * @param menuPermissions  所有Menu权限
     * @return MenuNode
     */
    private MenuNode buildMenuNodeList(TPermission parentPermission, List<TPermission> menuPermissions) {
        List<MenuNode> children = menuPermissions.stream()
                .filter(permission -> permission.getParentId().equals(parentPermission.getId()))
                .map(permission -> MenuNode.builder()
                        .index(permission.getRoute())
                        .title(permission.getName())
                        .icon(permission.getIcon())
                        .children(null)
                        .disabled(false)
                        .build())
                .toList();
        if (children.isEmpty()) {
            children = null;
        }
        return MenuNode.builder()
                .index(parentPermission.getRoute())
                .title(parentPermission.getName())
                .icon(parentPermission.getIcon())
                .children(children)
                .disabled(false)
                .build();
    }
}
