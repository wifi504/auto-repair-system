package com.lhl.rp.service.impl;

import com.lhl.rp.bean.TRole;
import com.lhl.rp.repository.TRoleMapper;
import com.lhl.rp.service.TRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 通过ID查询角色信息
     *
     * @param id 角色ID
     * @return 角色Bean
     */
    @Override
    public TRole consultById(Long id) {
        return tRoleMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过ID更新角色信息
     *
     * @param tRole 带ID信息的角色Bean
     * @return 成功记录条数
     */
    @Override
    public int updateById(TRole tRole) {
        int count = tRoleMapper.updateByPrimaryKeySelective(tRole);
        if (count != 1) throw new RuntimeException("更新失败");
        return count;
    }

    /**
     * 通过ID删除角色信息
     *
     * @param id 主键ID
     * @return 成功记录条数
     */
    @Override
    public int deleteById(Long id) {
        int count = tRoleMapper.deleteByPrimaryKey(id);
        if (count != 1) throw new RuntimeException("删除失败");
        return count;
    }

    /**
     * 通过ID集合批量删除角色信息
     *
     * @param ids 主键ID List集合
     * @return 成功记录条数
     */
    @Override
    public int deleteByIds(List<Long> ids) {
        int count = tRoleMapper.deleteByPrimaryKeys(ids);
        if (count != ids.size()) throw new RuntimeException("部分条目删除失败");
        return count;
    }

    /**
     * 创建角色
     *
     * @param tRole 角色Bean
     * @return 成功记录条数
     */
    @Transactional
    @Override
    public int creatRole(TRole tRole) {
        if (consultById(tRole.getId()) != null) {
            throw new RuntimeException("该条目已存在");
        }
        int count = tRoleMapper.insert(tRole);
        if (count != 1) throw new RuntimeException("新建角色失败");
        return count;
    }
}
