package com.lhl.rp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/31_13:01
 */
@SpringBootTest
public class TRoleMapperTest {

    @Autowired
    private TRoleMapper tRoleMapper;

    @Test
    public void testSelectAll() {
        tRoleMapper.selectAll().forEach(System.out::println);
    }
}
