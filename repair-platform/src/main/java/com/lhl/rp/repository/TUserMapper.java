package com.lhl.rp.repository;

import com.lhl.rp.bean.TRole;
import com.lhl.rp.bean.TUser;

import java.util.List;

public interface TUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

    TUser selectByLoginAct(String username);

    List<TUser> selectAll();

    List<TUser> selectAllExist();

    int deleteByPrimaryKeys(List<Long> keys);

    List<TRole> selectRolesByUserId(long userId);

    int deleteUserRoles(Long userId);

    int addUserRoles(Long userId, List<Long> roleIds);

    List<String> queryPermissionCodes(long userId);
}