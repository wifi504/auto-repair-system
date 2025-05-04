package com.lhl.rp.service;

import com.lhl.rp.bean.TRole;
import com.lhl.rp.bean.TUser;
import com.lhl.rp.dto.UserDto;
import com.lhl.rp.dto.UserRegisterDto;
import com.lhl.rp.service.exception.TUserServiceException;

import java.util.List;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_2:52
 */
public interface TUserService {

    /**
     * 查询所有用户
     */
    List<TUser> selectAll();

    /**
     * 查询所有存在的用户（排除逻辑删除）
     */
    List<TUser> selectAllExist();

    /**
     * 根据登录名查用户
     */
    TUser selectByName(String username);

    /**
     * 根据主键查用户
     */
    TUser selectById(Long id);

    /**
     * 根据主键删除用户
     */
    int deleteById(Long id) throws TUserServiceException;

    /**
     * 根据主键批量删除用户
     */
    int deleteByIds(List<Long> idList) throws TUserServiceException;

    /**
     * 增加用户
     */
    Long insert(UserDto userDto) throws TUserServiceException;

    /**
     * 用户注册
     */
    Long register(UserRegisterDto userRegisterDto) throws TUserServiceException;

    /**
     * 根据主键更新用户
     */
    int updateById(UserDto userDto) throws TUserServiceException;

    /**
     * 根据主键更新用户
     */
    int updateById(TUser tUser);

    /**
     * 根据主键批量更新用户
     */
    int updateByIds(List<TUser> userList) throws TUserServiceException;

    /**
     * 重置密码
     */
    int resetPwd(Long id) throws TUserServiceException;

    /**
     * 批量重置密码
     */
    int resetPwdList(List<UserDto> userDtoList) throws TUserServiceException;

    /**
     * 封禁用户
     */
    int banUser(Long id) throws TUserServiceException;

    /**
     * 解封用户
     */
    int unbanUser(Long id) throws TUserServiceException;

    /**
     * 批量封禁
     */
    int banList(List<UserDto> userDtoList) throws TUserServiceException;

    /**
     * 批量解封
     */
    int unbanList(List<UserDto> userDtoList) throws TUserServiceException;

    /**
     * 逻辑删除用户
     */
    int removeByLogic(UserDto userDto) throws TUserServiceException;

    /**
     * 逻辑批量删除用户
     */
    int removeListByLogic(List<UserDto> userDtoList) throws TUserServiceException;

    /**
     * 根据主键查询用户所拥有的角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    List<TRole> consultAllRolesByUserId(long userId);

    /**
     * 更新用户角色列表
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     * @return 更新数量
     */
    int updateUserRoles(Long userId, List<Long> roleIds) throws TUserServiceException;

    /**
     * 根据用户查询用户权限
     *
     * @param userId 用户ID
     * @return 权限标识符列表
     */
    List<String> queryPermissionCodes(long userId);
}
