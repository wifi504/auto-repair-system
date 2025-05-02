package com.lhl.rp.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhl.rp.bean.LoginUser;
import com.lhl.rp.bean.TRole;
import com.lhl.rp.bean.TUser;
import com.lhl.rp.dto.RoleIdsDto;
import com.lhl.rp.dto.UserDto;
import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import com.lhl.rp.service.TUserService;
import com.lhl.rp.util.TokenCacheHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 新增用户
     */
    @Transactional
    @PreAuthorize("hasAuthority('user:create')")
    @PostMapping("/create")
    public R<?> create(@RequestBody UserDto userDto) {
        if (userDto == null) {
            return R.error(ResultCode.FAIL, "错误的请求体");
        }
        if (userDto.getLoginAct() == null || userDto.getLoginAct().isEmpty()) {
            return R.error(ResultCode.FAIL, "缺少用户登录名");
        }
        if (tUserService.selectByName(userDto.getLoginAct()) != null) {
            return R.error(ResultCode.FAIL, "该用户已存在");
        }
        TUser tUser = mappingDto(userDto);
        tUser.setStatus((byte) 1);
        if (userDto.getLoginPwd() == null || userDto.getLoginPwd().isEmpty()) {
            tUser.setLoginPwd(bCryptPasswordEncoder.encode("pwd123456"));
        }
        tUser.setCreateTime(new Date());
        tUserService.insert(tUser);
        TUser realTUser = tUserService.selectByName(tUser.getLoginAct());
        return R.ok(realTUser, "已创建用户：" + realTUser.getNickname() + "！");
    }

    /**
     * 修改用户信息
     */
    @Transactional
    @PreAuthorize("hasAuthority('user:edit')")
    @PutMapping("/edit")
    public R<?> edit(@RequestBody UserDto userDto) {
        TUser tUser = tUserService.selectById(userDto.getId());
        if (tUser == null) {
            return R.error(ResultCode.FAIL, "用户不存在");
        }
        tUser.setNickname(userDto.getNickname());
        tUser.setPhone(userDto.getPhone());
        tUser.setEmail(userDto.getEmail());
        tUser.setAvatarUrl(userDto.getAvatarUrl());
        int count = tUserService.updateById(tUser);
        return R.ok(null, "已更新" + count + "个用户");
    }

    /**
     * 重置用户密码
     */
    @Transactional
    @PreAuthorize("hasAuthority('user:reset-pwd')")
    @GetMapping("/reset-pwd/{id}")
    public R<?> resetPwd(@PathVariable() Long id) {
        TUser tUser = tUserService.selectById(id);
        if (tUser == null || tUser.getStatus() == 2) {
            return R.error(ResultCode.FAIL, "用户不存在");
        }
        tUser.setLoginPwd(bCryptPasswordEncoder.encode("pwd123456"));
        int count = tUserService.updateById(tUser);
        return R.ok(null, "已更新" + count + "个用户");
    }

    /**
     * 批量重置用户密码
     */
    @Transactional
    @PreAuthorize("hasAuthority('user:reset-pwd-list')")
    @PostMapping("/reset-pwd-list")
    public R<?> resetPwd(@RequestBody List<UserDto> userDtoList) {
        int count = 0;
        for (UserDto userDto : userDtoList) {
            TUser tUser = tUserService.selectById(userDto.getId());
            if (tUser == null || tUser.getStatus() == 2) {
                return R.error(ResultCode.FAIL, "用户不存在");
            }
            tUser.setLoginPwd(bCryptPasswordEncoder.encode("pwd123456"));
            count += tUserService.updateById(tUser);
        }
        return R.ok(null, "已更新" + count + "个用户");
    }

    /**
     * 封禁用户
     */
    @Transactional
    @PreAuthorize("hasAuthority('user:ban')")
    @GetMapping("/ban/{id}")
    public R<?> ban(@PathVariable Long id) {
        TUser tUser = tUserService.selectById(id);
        if (tUser == null || tUser.getStatus() != 1) {
            return R.error(ResultCode.FAIL, "用户不存在或已被封禁");
        }
        if (tUser.getId() == 1) {
            return R.error(ResultCode.FAIL, "系统管理员不可封禁！");
        }
        tUser.setStatus((byte) 0);
        int count = tUserService.updateById(tUser);
        // 登出该用户
        TokenCacheHolder.removeAll(tUser.getLoginAct());
        return R.ok(null, "已封禁" + count + "个用户");
    }

    /**
     * 解封用户
     */
    @Transactional
    @PreAuthorize("hasAuthority('user:unban')")
    @GetMapping("/unban/{id}")
    public R<?> unban(@PathVariable Long id) {
        TUser tUser = tUserService.selectById(id);
        if (tUser == null || tUser.getStatus() != 0) {
            return R.error(ResultCode.FAIL, "用户不存在或未被封禁");
        }
        tUser.setStatus((byte) 1);
        int count = tUserService.updateById(tUser);
        return R.ok(null, "已解封" + count + "个用户");
    }

    /**
     * 批量封禁用户
     */
    @Transactional
    @PreAuthorize("hasAuthority('user:ban-list')")
    @PostMapping("/ban-list")
    public R<?> banList(@RequestBody List<UserDto> userDtoList) {
        ArrayList<TUser> tUsers = new ArrayList<>();
        userDtoList.forEach(userDto -> {
            if (userDto != null && userDto.getStatus() == 1) {
                tUsers.add(mappingDto(userDto));
            }
        });
        AtomicInteger count = new AtomicInteger();
        tUsers.forEach(tUser -> {
            if (tUser.getId() == 1) {
                throw new RuntimeException("系统管理员不可封禁！");
            }
            tUser.setStatus((byte) 0);
            count.addAndGet(tUserService.updateById(tUser));
            // 登出该用户
            TokenCacheHolder.removeAll(tUser.getLoginAct());
        });
        if (count.get() != tUsers.size()) {
            throw new RuntimeException("部分用户封禁失败，已回滚");
        }
        return R.ok(null, "已封禁" + count + "个用户");
    }

    /**
     * 批量解封用户
     */
    @Transactional
    @PreAuthorize("hasAuthority('user:unban-list')")
    @PostMapping("/unban-list")
    public R<?> unbanList(@RequestBody List<UserDto> userDtoList) {
        ArrayList<TUser> tUsers = new ArrayList<>();
        userDtoList.forEach(userDto -> {
            if (userDto != null && userDto.getStatus() == 0) {
                tUsers.add(mappingDto(userDto));
            }
        });
        AtomicInteger count = new AtomicInteger();
        tUsers.forEach(tUser -> {
            tUser.setStatus((byte) 1);
            count.addAndGet(tUserService.updateById(tUser));
        });
        if (count.get() != tUsers.size()) {
            throw new RuntimeException("部分用户解封失败，已回滚");
        }
        return R.ok(null, "已解封" + count + "个用户");
    }

    /**
     * 逻辑删除用户
     */
    @Transactional
    @PreAuthorize("hasAuthority('user:remove')")
    @DeleteMapping("/remove")
    public R<?> remove(@RequestBody UserDto userDto) {
        if (userDto == null) {
            return R.error(ResultCode.FAIL, "请求数据异常");
        }
        TUser tUser = tUserService.selectById(userDto.getId());
        if (tUser == null) {
            return R.error(ResultCode.FAIL, "用户不存在");
        }
        if (tUser.getId() == 1) {
            return R.error(ResultCode.FAIL, "系统管理员不可删除！");
        }
        tUser.setStatus((byte) 2);
        // 登出该用户
        TokenCacheHolder.removeAll(tUser.getLoginAct());
        return R.ok(null, "已删除" + tUserService.updateById(tUser) + "个用户");
    }

    /**
     * 批量逻辑删除用户
     */
    @Transactional
    @PreAuthorize("hasAuthority('user:remove-all')")
    @DeleteMapping("/remove-all")
    public R<?> removeAll(@RequestBody List<UserDto> userDtoList) {
        ArrayList<TUser> tUsers = new ArrayList<>();
        for (UserDto userDto : userDtoList) {
            if (userDto != null) {
                TUser tUser = tUserService.selectById(userDto.getId());
                if (tUser == null || tUser.getStatus() == 2) {
                    return R.error(ResultCode.FAIL, "部分用户不存在");
                }
                if (tUser.getId() == 1) {
                    throw new RuntimeException("系统管理员不可删除！");
                }
                tUser.setStatus((byte) 2);
                tUsers.add(tUser);
            }
        }
        int count = tUserService.updateByIds(tUsers);
        if (count != tUsers.size()) {
            throw new RuntimeException("部分用户删除失败，已回滚");
        }
        // 登出这些用户
        tUsers.forEach(tUser -> TokenCacheHolder.removeAll(tUser.getLoginAct()));
        return R.ok(null, "已删除" + count + "个用户");
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
        return null;
    }

    /**
     * 映射DTO到实体类
     *
     * @param userDto DTO
     * @return TUser
     */
    private TUser mappingDto(UserDto userDto) {
        return TUser.builder()
                .id(userDto.getId())
                .loginAct(userDto.getLoginAct())
                .loginPwd(userDto.getLoginPwd())
                .realName(userDto.getRealName())
                .nickname(userDto.getNickname())
                .phone(userDto.getPhone())
                .email(userDto.getEmail())
                .avatarUrl(userDto.getAvatarUrl())
                .status(userDto.getStatus())
                .createTime(userDto.getCreateTime())
                .build();
    }
}
