package com.lhl.rp.service;

import com.lhl.rp.bean.TUser;

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
    int deleteById(Long id);

    /**
     * 根据主键批量删除用户
     */
    int deleteByIds(List<Long> idList);

    /**
     * 增加用户
     */
    int insert(TUser tUser);

    /**
     * 根据主键更新用户
     */
    int updateById(TUser tUser);

    /**
     * 根据主键批量更新用户
     */
    int updateByIds(List<TUser> userList);
}
