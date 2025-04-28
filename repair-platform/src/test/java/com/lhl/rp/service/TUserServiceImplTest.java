package com.lhl.rp.service;

import com.lhl.rp.bean.TRole;
import com.lhl.rp.bean.TUser;
import com.lhl.rp.repository.TUserMapper;
import com.lhl.rp.service.impl.TUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/1_22:24
 */
@SpringBootTest
public class TUserServiceImplTest {
    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TUserServiceImpl tUserService;

    private TUser testUser;
    private List<TUser> testUserList;

    @BeforeEach
    void setUp() {
        testUser = new TUser();
        testUser.setId(1L);
        testUser.setLoginAct("testUser");
        testUser.setLoginPwd("password");
        testUser.setRealName("Test User");
        testUser.setStatus((byte) 1);
        testUser.setCreateTime(new Date());

        TUser testUser2 = new TUser();
        testUser2.setId(2L);
        testUser2.setLoginAct("testUser2");

        testUserList = Arrays.asList(testUser, testUser2);
    }

    @Test
    void selectAll() {
        List<TUser> tUsers = tUserService.selectAll();
        assertNotNull(tUsers);
        tUsers.forEach(System.out::println);
    }

    @Test
    void selectByName() {
        TUser testUser1 = tUserService.selectByName("testUser");
        assertNull(testUser1);
        TUser testUser2 = tUserService.selectByName("admin");
        assertNotNull(testUser2);
        System.out.println(testUser2);
    }

    @Test
    void selectById() {
        TUser tUser = tUserService.selectById(1L);
        assertNotNull(tUser);
        System.out.println(tUser);
    }

    @Test
    public void testConsultAllRolesByUserId() {
        List<TRole> tRoles = tUserMapper.selectRolesByUserId(1);
        assertNotNull(tRoles);
        tRoles.forEach(System.out::println);
    }

    @Test
    public void testUpdateUserRoles() {
        int count = tUserService.updateUserRoles(74L, Arrays.asList(2L, 5L, 6L));
        assertEquals(3, count);
        System.out.println(count);
    }
}
