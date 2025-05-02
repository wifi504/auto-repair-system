package com.lhl.rp.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhl.rp.bean.TRole;
import com.lhl.rp.dto.PermissionIdsDto;
import com.lhl.rp.dto.RoleDto;
import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import com.lhl.rp.service.TPermissionService;
import com.lhl.rp.service.TRoleService;
import com.lhl.rp.service.exception.TRoleServiceException;
import com.lhl.rp.service.impl.TPermissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/31_11:50
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    TRoleService tRoleService;

    @Autowired
    TPermissionService tPermissionService;

    /**
     * 查询角色
     */
    @PreAuthorize("hasAuthority('role:view')")
    @GetMapping("/view")
    public R<PageInfo<TRole>> view(@RequestParam(defaultValue = "1") Integer pageNo,
                                   @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageHelper.startPage(pageNo, pageSize);
        List<TRole> tRoles = tRoleService.consultAllRole();
        PageHelper.clearPage();
        return R.ok(new PageInfo<>(tRoles));
    }

    /**
     * 修改角色
     */
    @PreAuthorize("hasAuthority('role:update')")
    @PutMapping("/update")
    public R<?> update(@RequestBody RoleDto roleDto) {
        TRole tRole = tRoleService.consultById(roleDto.getId());
        tRole.setName(roleDto.getName());
        tRole.setCode(roleDto.getCode());
        tRole.setRemark(roleDto.getRemark());
        tRole.setOrderNum(roleDto.getOrderNum());
        try {
            return R.ok(null, "成功更新" + tRoleService.updateById(tRole) + "条角色数据！");
        } catch (TRoleServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @PreAuthorize("hasAuthority('role:delete')")
    @DeleteMapping("/delete")
    public R<?> delete(@RequestBody List<RoleDto> roleDtoList) {
        try {
            int count;
            if (roleDtoList.size() == 1) {
                count = tRoleService.deleteById(roleDtoList.get(0).getId());
            } else {
                // 批量删除
                List<Long> ids = new ArrayList<>();
                roleDtoList.forEach(roleDto -> ids.add(roleDto.getId()));
                count = tRoleService.deleteByIds(ids);
            }
            return R.ok(null, "成功删除" + count + "条角色数据！");
        } catch (TRoleServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }


    /**
     * 新增角色
     */
    @PreAuthorize("hasAuthority('role:create')")
    @PostMapping("/create")
    public R<?> create(@RequestBody RoleDto roleDto) {
        TRole newRole = TRole.builder()
                .name(roleDto.getName())
                .code(roleDto.getCode())
                .remark(roleDto.getRemark())
                .orderNum(roleDto.getOrderNum())
                .build();
        try {
            tRoleService.creatRole(newRole);
            TRole tRole = tRoleService.consultByCode(newRole.getCode());
            return R.ok(tRole, "成功创建角色：" + tRole.getName() + "！");
        } catch (TRoleServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    /**
     * 获取系统权限列表
     */
    @PreAuthorize("hasAuthority('role:list-all-permission')")
    @GetMapping("/list-all-permission")
    public R<?> listAllPermission() {
        // 查询权限表所有数据
        List<TPermissionServiceImpl.PermissionNode> list = tPermissionService.consultAllPermissionByTree();
        // 返回前端
        return R.ok(list);
    }

    /**
     * 根据角色查询角色权限
     */
    @PreAuthorize("hasAuthority('role:list-permission')")
    @GetMapping("/list-permission/{roleId}")
    public R<?> listPermission(@PathVariable String roleId) {
        try {
            return R.ok(tRoleService.listPermissionIds(Long.parseLong(roleId)));
        } catch (NumberFormatException e) {
            return R.error(ResultCode.FAIL, "角色ID应为数字");
        }
    }

    /**
     * 编辑角色权限列表
     */
    @PreAuthorize("hasAuthority('role:edit-permission')")
    @PutMapping("/edit-permission")
    public R<?> editPermission(@RequestBody PermissionIdsDto permissionIdsDto) {
        try {
            // 更新角色权限
            int count = tRoleService.updateRolePermissions(
                    permissionIdsDto.getRoleId(),
                    permissionIdsDto.getIdList());
            return R.ok(null, "已更新" + count + "个角色权限");
        } catch (Exception e) {
            return R.error(ResultCode.FAIL, "更新失败" + e.getMessage());
        }
    }
}