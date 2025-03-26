package com.lhl.rp.repository;

import com.lhl.rp.bean.TUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_0:59
 */
@SpringBootTest
public class TUserMapperTest {

    @Autowired
    private TUserMapper userMapper;

    @Test
    public void testSelectByLoginAct() {
        TUser user = userMapper.selectByLoginAct("admin");
        assertNotNull(user, "用户不存在");
        assertEquals("admin", user.getLoginAct());
        System.out.println("用户信息：" + user);
    }
}
