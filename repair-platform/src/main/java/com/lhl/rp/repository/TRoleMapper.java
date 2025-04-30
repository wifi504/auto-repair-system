package com.lhl.rp.repository;

import com.lhl.rp.bean.TPermission;
import com.lhl.rp.bean.TRole;

import java.util.List;

public interface TRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TRole record);

    int insertSelective(TRole record);

    TRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TRole record);

    int updateByPrimaryKey(TRole record);

    List<TRole> selectAll();

    int deleteByPrimaryKeys(List<Long> ids);

    List<TPermission> listPermission(Long roleId);

    int deleteRolePermissions(Long roleId);

    int addRolePermissions(Long roleId, List<Long> permissionIds);

    TRole selectByCode(String code);
}