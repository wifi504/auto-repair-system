package com.lhl.rp.service.impl;

import com.lhl.rp.bean.TRole;
import com.lhl.rp.repository.TRoleMapper;
import com.lhl.rp.service.TRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/31_12:57
 */
@Service
public class TRoleServiceImpl implements TRoleService {

    @Autowired
    private TRoleMapper tRoleMapper;

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<TRole> consultAllRole() {
        return tRoleMapper.selectAll();
    }
}
