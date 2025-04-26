package com.lhl.rp.service.impl;

import com.lhl.rp.bean.TUser;
import com.lhl.rp.repository.TUserMapper;
import com.lhl.rp.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/1_22:04
 */
@Service
public class TUserServiceImpl implements TUserService {

    @Autowired
    TUserMapper tUserMapper;

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
    public int deleteById(Long id) {
        int count = tUserMapper.deleteByPrimaryKey(id);
        if (count != 1) throw new RuntimeException("用户不存在！");
        return count;
    }

    /**
     * 根据主键批量删除用户
     *
     * @param idList 主键 List
     */
    @Transactional
    @Override
    public int deleteByIds(List<Long> idList) {
        int count = tUserMapper.deleteByPrimaryKeys(idList);
        if (count != idList.size()) throw new RuntimeException("部分条目删除失败，已回滚");
        return count;
    }

    /**
     * 增加用户
     *
     * @param tUser 用户 Bean
     */
    @Transactional
    @Override
    public int insert(TUser tUser) {
        if (selectById(tUser.getId()) != null) {
            throw new RuntimeException("该用户当前已存在！");
        }
        int count = tUserMapper.insert(tUser);
        if (count != 1) throw new RuntimeException("数据增添失败！");
        return count;
    }

    /**
     * 根据主键更新用户
     *
     * @param tUser 用户 Bean
     */
    @Override
    public int updateById(TUser tUser) {
        if (tUserMapper.selectByPrimaryKey(tUser.getId()) == null) {
            throw new RuntimeException("不存在此用户！");
        }
        int count = tUserMapper.updateByPrimaryKeySelective(tUser);
        if (count != 1) throw new RuntimeException("数据更新失败！");
        return count;
    }

    /**
     * 根据主键批量更新用户
     *
     * @param userList 用户 BeanList
     */
    @Transactional
    @Override
    public int updateByIds(List<TUser> userList) {
        if (userList == null || userList.isEmpty()) {
            throw new RuntimeException("条目为空");
        }
        int count = 0;
        for (TUser tUser : userList) {
            count += tUserMapper.updateByPrimaryKey(tUser);
        }
        if (count != userList.size()) throw new RuntimeException("部分条目更新失败，已回滚");
        return count;
    }
}
