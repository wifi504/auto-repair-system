package com.lhl.rp.service;

import com.lhl.rp.bean.TPermission;
import com.lhl.rp.service.exception.TRoleServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/30_14:38
 */
@SpringBootTest
public class TRoleServiceImplTest {

    @Autowired
    TRoleService tRoleService;

    @Test
    public void testListPermission() {
        List<TPermission> tPermissions = tRoleService.listPermission(1L);
        assertNotNull(tPermissions);
        tPermissions.forEach(System.out::println);
    }

    @Test
    public void testListPermissionIds() {
        List<Long> idList = tRoleService.listPermissionIds(5L);
        assertNotNull(idList);
        System.out.println(idList);
    }

    @Test
    public void testUpdateRolePermissions() throws TRoleServiceException {
        List<Long> permissionIds = List.of(10101L, 10102L, 10103L);
        int count = tRoleService.updateRolePermissions(2L, permissionIds);
    }
}
