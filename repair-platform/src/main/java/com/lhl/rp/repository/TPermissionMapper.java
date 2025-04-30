package com.lhl.rp.repository;

import com.lhl.rp.bean.TPermission;

import java.util.List;

public interface TPermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TPermission record);

    int insertSelective(TPermission record);

    TPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TPermission record);

    int updateByPrimaryKey(TPermission record);

    List<TPermission> consultAllPermission();
}