package com.lhl.rp.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhl.rp.bean.TRole;
import com.lhl.rp.bean.TUser;
import com.lhl.rp.dto.RoleIdsDto;
import com.lhl.rp.dto.UserDto;
import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import com.lhl.rp.service.TUserService;
import com.lhl.rp.service.exception.TUserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息接口
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/26_23:59
 */

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private TUserService tUserService;

    /**
     * 新增用户
     */
    @PreAuthorize("hasAuthority('user:create')")
    @PostMapping("/create")
    public R<?> create(@RequestBody UserDto userDto) {
        try {
            return R.ok(tUserService.insert(userDto));
        } catch (TUserServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    /**
     * 修改用户信息
     */
    @PreAuthorize("hasAuthority('user:edit')")
    @PutMapping("/edit")
    public R<?> edit(@RequestBody UserDto userDto) {
        try {
            return R.ok(null, "已更新" + tUserService.updateById(userDto) + "个用户");
        } catch (TUserServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    /**
     * 重置用户密码
     */
    @PreAuthorize("hasAuthority('user:reset-pwd')")
    @GetMapping("/reset-pwd/{id}")
    public R<?> resetPwd(@PathVariable() Long id) {
        try {
            return R.ok(null, "已更新" + tUserService.resetPwd(id) + "个用户");
        } catch (TUserServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    /**
     * 批量重置用户密码
     */
    @PreAuthorize("hasAuthority('user:reset-pwd-list')")
    @PostMapping("/reset-pwd-list")
    public R<?> resetPwd(@RequestBody List<UserDto> userDtoList) {
        try {
            return R.ok(null, "已重置" + tUserService.resetPwdList(userDtoList) + "个用户");
        } catch (TUserServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    /**
     * 封禁用户
     */
    @PreAuthorize("hasAuthority('user:ban')")
    @GetMapping("/ban/{id}")
    public R<?> ban(@PathVariable Long id) {

        try {
            return R.ok(null, "已封禁" + tUserService.banUser(id) + "个用户");
        } catch (TUserServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    /**
     * 解封用户
     */
    @PreAuthorize("hasAuthority('user:unban')")
    @GetMapping("/unban/{id}")
    public R<?> unban(@PathVariable Long id) {
        try {
            return R.ok(null, "已解封" + tUserService.unbanUser(id) + "个用户");
        } catch (TUserServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    /**
     * 批量封禁用户
     */
    @PreAuthorize("hasAuthority('user:ban-list')")
    @PostMapping("/ban-list")
    public R<?> banList(@RequestBody List<UserDto> userDtoList) {
        try {
            return R.ok(null, "已封禁" + tUserService.banList(userDtoList) + "个用户");
        } catch (TUserServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    /**
     * 批量解封用户
     */
    @PreAuthorize("hasAuthority('user:unban-list')")
    @PostMapping("/unban-list")
    public R<?> unbanList(@RequestBody List<UserDto> userDtoList) {
        try {
            return R.ok(null, "已解封" + tUserService.unbanList(userDtoList) + "个用户");
        } catch (TUserServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    /**
     * 逻辑删除用户
     */
    @PreAuthorize("hasAuthority('user:remove')")
    @DeleteMapping("/remove")
    public R<?> remove(@RequestBody UserDto userDto) {
        try {
            return R.ok(null, "已删除" + tUserService.removeByLogic(userDto) + "个用户");
        } catch (TUserServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    /**
     * 批量逻辑删除用户
     */
    @PreAuthorize("hasAuthority('user:remove-all')")
    @DeleteMapping("/remove-all")
    public R<?> removeAll(@RequestBody List<UserDto> userDtoList) {
        try {
            return R.ok(null, "已删除" + tUserService.removeListByLogic(userDtoList) + "个用户");
        } catch (TUserServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    /**
     * 物理删除用户
     */
    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/delete")
    public R<?> delete(@RequestBody UserDto userDto) {
        return R.error(ResultCode.FORBIDDEN, "暂时不允许直接删除用户数据！");
    }

    /**
     * 批量物理删除用户
     */
    @PreAuthorize("hasAuthority('user:delete-all')")
    @DeleteMapping("/delete-all")
    public R<?> deleteAll(@RequestBody List<UserDto> userDtoList) {
        return R.error(ResultCode.FORBIDDEN, "暂时不允许直接删除用户数据！");
    }

    /**
     * 查询用户
     */
    @PreAuthorize("hasAuthority('user:view')")
    @GetMapping("/view/{id}")
    public R<?> view(@PathVariable Long id) {
        TUser tUser = tUserService.selectById(id);
        if (tUser == null || tUser.getStatus() == 2) {
            return R.error(ResultCode.FAIL, "用户不存在");
        }
        return R.ok(tUser);
    }

    /**
     * 查询用户列表（屏蔽逻辑删除）
     */
    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping("/list")
    public R<PageInfo<TUser>> list(@RequestParam(defaultValue = "1") Integer pageNo,
                                   @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageHelper.startPage(pageNo, pageSize);
        List<TUser> tUsers = tUserService.selectAllExist();
        PageHelper.clearPage();
        return R.ok(new PageInfo<>(tUsers));
    }

    /**
     * 根据用户查询用户角色
     *
     * @param id 用户ID
     * @return 角色列表
     */
    @PreAuthorize("hasAuthority('user:list-roles')")
    @GetMapping("/list-roles/{id}")
    public R<?> listRoles(@PathVariable String id) {
        try {
            List<TRole> tRoles = tUserService.consultAllRolesByUserId(Long.parseLong(id));
            return R.ok(tRoles);
        } catch (NumberFormatException e) {
            return R.error(ResultCode.FAIL, "用户ID格式错误");
        }
    }

    /**
     * 编辑用户角色列表
     *
     * @param roleIdsDto 角色ID列表
     * @return 操作状态
     */
    @PreAuthorize("hasAuthority('user:edit-roles')")
    @PutMapping("/edit-roles")
    public R<?> editRoles(@RequestBody RoleIdsDto roleIdsDto) {
        try {
            int count = tUserService.updateUserRoles(roleIdsDto.getUserId(), roleIdsDto.getIdList());
            return R.ok(null, "已更新" + count + "个用户角色");
        } catch (Exception e) {
            return R.error(ResultCode.FAIL, "更新失败" + e.getMessage());
        }
    }

    /**
     * 根据用户查询用户权限
     *
     * @param id 用户ID
     * @return 权限标识符列表
     */
    @PreAuthorize("hasAuthority('user:list-permission')")
    @GetMapping("/list-permission/{id}")
    public R<?> listPermission(@PathVariable String id) {
        List<String> permissionCodes = tUserService.queryPermissionCodes(Long.parseLong(id));
        return R.ok(permissionCodes);
    }
}
