package com.lhl.rp.service.impl;

import com.lhl.rp.bean.TRole;
import com.lhl.rp.bean.TUser;
import com.lhl.rp.dto.UserDto;
import com.lhl.rp.dto.UserRegisterDto;
import com.lhl.rp.repository.TUserMapper;
import com.lhl.rp.service.TUserService;
import com.lhl.rp.service.exception.TUserServiceException;
import com.lhl.rp.util.TokenCacheHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/1_22:04
 */
@Service
public class TUserServiceImpl implements TUserService {

    @Autowired
    TUserMapper tUserMapper;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * 查询所有用户
     */
    @Override
    public List<TUser> selectAll() {
        return tUserMapper.selectAll();
    }

    /**
     * 查询所有存在的用户（排除逻辑删除）
     */
    @Override
    public List<TUser> selectAllExist() {
        return tUserMapper.selectAllExist();
    }

    /**
     * 根据登录名查用户
     *
     * @param username 用户唯一登录名
     */
    @Override
    public TUser selectByName(String username) {
        return tUserMapper.selectByLoginAct(username);
    }

    /**
     * 根据主键查用户
     *
     * @param id 主键
     */
    @Override
    public TUser selectById(Long id) {
        return tUserMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键删除用户
     *
     * @param id 主键
     */
    @Override
    public int deleteById(Long id) throws TUserServiceException {
        int count = tUserMapper.deleteByPrimaryKey(id);
        if (count != 1) throw new TUserServiceException("用户不存在！");
        return count;
    }

    /**
     * 根据主键批量删除用户
     *
     * @param idList 主键 List
     */
    @Transactional
    @Override
    public int deleteByIds(List<Long> idList) throws TUserServiceException {
        int count = tUserMapper.deleteByPrimaryKeys(idList);
        if (count != idList.size()) throw new TUserServiceException("部分条目删除失败，已回滚");
        return count;
    }

    /**
     * 增加用户
     *
     * @param userDto 用户DTO
     */
    @Transactional
    @Override
    public Long insert(UserDto userDto) throws TUserServiceException {
        BCryptPasswordEncoder bCryptPasswordEncoder = applicationContext.getBean(BCryptPasswordEncoder.class);
        if (userDto.getLoginAct() == null || userDto.getLoginAct().isEmpty()) {
            throw new TUserServiceException("用户名不能为空");
        }
        if (tUserMapper.selectByLoginAct(userDto.getLoginAct()) != null) {
            throw new TUserServiceException("用户已存在");
        }
        TUser tUser = mappingUserDto(userDto);
        tUser.setStatus((byte) 1);
        if (userDto.getLoginPwd() == null || userDto.getLoginPwd().isEmpty()) {
            tUser.setLoginPwd(bCryptPasswordEncoder.encode("pwd123456"));
        }
        tUser.setCreateTime(new Date());
        int count = tUserMapper.insert(tUser);
        if (count != 1) throw new TUserServiceException("数据增添失败！");
        return tUserMapper.selectByLoginAct(tUser.getLoginAct()).getId();
    }

    /**
     * 用户注册
     */
    @Transactional
    @Override
    public Long register(UserRegisterDto userRegisterDto) throws TUserServiceException {
        TUser tUser = mappingUserRegisterDto(userRegisterDto);
        if (tUserMapper.selectByLoginAct(tUser.getLoginAct()) != null) {
            throw new TUserServiceException("用户已存在");
        }
        tUser.setStatus((byte) 1);
        tUser.setCreateTime(new Date());
        if (tUserMapper.insert(tUser) != 1) {
            throw new TUserServiceException("数据增添失败！");
        }
        Long id = tUserMapper.selectByLoginAct(tUser.getLoginAct()).getId();
        updateUserRoles(id, new ArrayList<>());
        return id;
    }

    /**
     * 根据主键更新用户
     *
     * @param userDto 用户
     */
    @Override
    public int updateById(UserDto userDto) throws TUserServiceException {
        TUser tUser = tUserMapper.selectByPrimaryKey(userDto.getId());
        if (tUser == null) {
            throw new TUserServiceException("不存在此用户！");
        }
        tUser.setNickname(userDto.getNickname());
        tUser.setPhone(userDto.getPhone());
        tUser.setEmail(userDto.getEmail());
        tUser.setAvatarUrl(userDto.getAvatarUrl());
        int count = tUserMapper.updateByPrimaryKeySelective(tUser);
        if (count != 1) throw new TUserServiceException("数据更新失败！");
        return count;
    }

    /**
     * 根据主键批量更新用户
     *
     * @param userList 用户 BeanList
     */
    @Transactional
    @Override
    public int updateByIds(List<TUser> userList) throws TUserServiceException {
        if (userList == null || userList.isEmpty()) {
            throw new TUserServiceException("条目为空");
        }
        int count = 0;
        for (TUser tUser : userList) {
            count += tUserMapper.updateByPrimaryKey(tUser);
        }
        if (count != userList.size()) throw new TUserServiceException("部分条目更新失败，已回滚");
        return count;
    }

    /**
     * 重置密码
     */
    @Transactional
    @Override
    public int resetPwd(Long id) throws TUserServiceException {
        BCryptPasswordEncoder bCryptPasswordEncoder = applicationContext.getBean(BCryptPasswordEncoder.class);
        TUser tUser = tUserMapper.selectByPrimaryKey(id);
        if (tUser == null || tUser.getStatus() == 2) {
            throw new TUserServiceException("用户不存在");
        }
        tUser.setLoginPwd(bCryptPasswordEncoder.encode("pwd123456"));
        int count = tUserMapper.updateByPrimaryKey(tUser);
        if (count != 1) throw new TUserServiceException("数据更新失败！");
        return count;
    }

    /**
     * 批量重置密码
     */
    @Transactional
    @Override
    public int resetPwdList(List<UserDto> userDtoList) throws TUserServiceException {
        BCryptPasswordEncoder bCryptPasswordEncoder = applicationContext.getBean(BCryptPasswordEncoder.class);
        ArrayList<TUser> tUsers = new ArrayList<>();
        userDtoList.forEach(userDto -> tUsers.add(tUserMapper.selectByPrimaryKey(userDto.getId())));
        tUsers.forEach(tUser -> tUser.setLoginPwd(bCryptPasswordEncoder.encode("pwd123456")));
        int count = updateByIds(tUsers);
        if (count != tUsers.size()) throw new TUserServiceException("部分用户重置失败，已回滚");
        return count;
    }

    /**
     * 封禁用户
     */
    @Transactional
    @Override
    public int banUser(Long id) throws TUserServiceException {
        TUser tUser = tUserMapper.selectByPrimaryKey(id);
        if (tUser == null || tUser.getStatus() != 1) {
            throw new TUserServiceException("用户不存在或已被封禁");
        }
        if (tUser.getId() == 1) {
            throw new TUserServiceException("系统管理员不可封禁");
        }
        tUser.setStatus((byte) 0);
        int count = tUserMapper.updateByPrimaryKey(tUser);
        if (count != 1) throw new TUserServiceException("执行封禁失败");
        // 注销用户所有登录态
        TokenCacheHolder.removeAll(tUser.getLoginAct());
        return count;
    }

    /**
     * 解封用户
     */
    @Transactional
    @Override
    public int unbanUser(Long id) throws TUserServiceException {
        TUser tUser = tUserMapper.selectByPrimaryKey(id);
        if (tUser == null || tUser.getStatus() != 0) {
            throw new TUserServiceException("用户不存在或未被封禁");
        }
        tUser.setStatus((byte) 1);
        return tUserMapper.updateByPrimaryKey(tUser);
    }

    /**
     * 批量封禁
     */
    @Transactional
    @Override
    public int banList(List<UserDto> userDtoList) throws TUserServiceException {
        ArrayList<TUser> tUsers = new ArrayList<>();
        userDtoList.forEach(userDto -> {
            // 管理员不可封禁
            if (userDto != null && userDto.getStatus() == 1 && userDto.getId() != 1) {
                tUsers.add(selectById(userDto.getId()));
            }
        });
        AtomicInteger count = new AtomicInteger();
        tUsers.forEach(tUser -> {
            tUser.setStatus((byte) 0);
            count.addAndGet(tUserMapper.updateByPrimaryKey(tUser));
            TokenCacheHolder.removeAll(tUser.getLoginAct());
        });
        if (count.get() != tUsers.size()) {
            throw new TUserServiceException("部分用户封禁失败，已回滚");
        }
        return count.get();
    }

    /**
     * 批量解封
     */
    @Transactional
    @Override
    public int unbanList(List<UserDto> userDtoList) throws TUserServiceException {
        ArrayList<TUser> tUsers = new ArrayList<>();
        userDtoList.forEach(userDto -> {
            if (userDto != null && userDto.getStatus() == 0) {
                tUsers.add(selectById(userDto.getId()));
            }
        });
        AtomicInteger count = new AtomicInteger();
        tUsers.forEach(tUser -> {
            tUser.setStatus((byte) 1);
            count.addAndGet(tUserMapper.updateByPrimaryKey(tUser));
        });
        if (count.get() != tUsers.size()) {
            throw new TUserServiceException("部分用户解封失败，已回滚");
        }
        return count.get();
    }

    /**
     * 逻辑删除用户
     */
    @Transactional
    @Override
    public int removeByLogic(UserDto userDto) throws TUserServiceException {
        TUser tUser = tUserMapper.selectByPrimaryKey(userDto.getId());
        if (tUser == null) {
            throw new TUserServiceException("用户不存在");
        }
        if (tUser.getId() == 1) {
            throw new TUserServiceException("系统管理员不可删除");
        }
        if (tUser.getStatus() == 2) {
            throw new TUserServiceException("用户已删除");
        }
        tUser.setStatus((byte) 2);
        // 登出该用户
        TokenCacheHolder.removeAll(tUser.getLoginAct());
        return tUserMapper.updateByPrimaryKey(tUser);
    }

    /**
     * 逻辑批量删除用户
     */
    @Transactional
    @Override
    public int removeListByLogic(List<UserDto> userDtoList) throws TUserServiceException {
        try {
            ArrayList<TUser> tUsers = new ArrayList<>();
            userDtoList.forEach(userDto -> {
                TUser tUser = tUserMapper.selectByPrimaryKey(userDto.getId());
                if (tUser == null || tUser.getStatus() == 2) {
                    throw new RuntimeException("部分用户不存在");
                }
                if (tUser.getId() == 1) {
                    throw new RuntimeException("系统管理员不可删除！");
                }
                tUser.setStatus((byte) 2);
                tUsers.add(tUser);
            });
            int count = updateByIds(tUsers);
            if (count != tUsers.size()) {
                throw new RuntimeException("部分用户删除失败，已回滚");
            }
            // 注销用户所有登录态
            tUsers.forEach(tUser -> TokenCacheHolder.removeAll(tUser.getLoginAct()));
            return count;
        } catch (Exception e) {
            throw new TUserServiceException(e.getMessage());
        }
    }

    /**
     * 根据主键查询用户所拥有的角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    @Override
    public List<TRole> consultAllRolesByUserId(long userId) {
        return tUserMapper.selectRolesByUserId(userId);
    }

    /**
     * 更新用户角色列表
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     * @return 更新数量
     */
    @Transactional
    @Override
    public int updateUserRoles(Long userId, List<Long> roleIds) throws TUserServiceException {
        TUser tUser = tUserMapper.selectByPrimaryKey(userId);
        if (tUser == null) throw new TUserServiceException("用户不存在");
        List<Long> ids = new ArrayList<>();
        if (roleIds != null) {
            ids.addAll(roleIds);
        }
        if (ids.isEmpty() || !ids.contains(6L)) {
            // TODO 默认添加游客角色的id，但是这种操作并没有考虑到角色表的动态性
            ids.add(6L);
        }
        // 1. 删除用户所有角色
        int c1 = tUserMapper.deleteUserRoles(userId);
        // 2. 添加用户角色
        int c2 = tUserMapper.addUserRoles(userId, ids);
        // 3. 检查是否成功
        if (c1 != -1 && c2 == ids.size()) {
            return c2;
        }
        throw new TUserServiceException("更新失败");
    }

    /**
     * 根据用户查询用户权限
     *
     * @param userId 用户ID
     * @return 权限标识符列表
     */
    @Override
    public List<String> queryPermissionCodes(long userId) {
        return tUserMapper.queryPermissionCodes(userId);
    }

    // 辅助方法：映射 UserDto
    private TUser mappingUserDto(UserDto userDto) {
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

    // 辅助方法：映射 UserRegisterDto
    private TUser mappingUserRegisterDto(UserRegisterDto userRegisterDto) throws TUserServiceException {
        if (userRegisterDto.getLoginAct() == null || userRegisterDto.getLoginAct().isEmpty()) {
            throw new TUserServiceException("用户名不能为空！");
        }
        if (!userRegisterDto.getLoginPwd().equals(userRegisterDto.getConfirmPwd())) {
            throw new TUserServiceException("两次密码不一致！");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = applicationContext.getBean(BCryptPasswordEncoder.class);
        return TUser.builder()
                .loginAct(userRegisterDto.getLoginAct().trim())
                .loginPwd(bCryptPasswordEncoder.encode(userRegisterDto.getLoginPwd().trim()))
                .nickname(userRegisterDto.getNickname().trim())
                .phone(userRegisterDto.getPhone().trim())
                .email(userRegisterDto.getEmail().trim())
                .build();
    }
}
