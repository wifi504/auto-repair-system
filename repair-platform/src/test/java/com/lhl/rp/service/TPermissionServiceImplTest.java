package com.lhl.rp.service;

import com.lhl.rp.bean.TPermission;
import com.lhl.rp.service.impl.TPermissionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/30_14:26
 */
@SpringBootTest
public class TPermissionServiceImplTest {

    @Autowired
    private TPermissionService tPermissionService;

    @Test
    public void testConsultAllPermission() {
        List<TPermission> tPermissions = tPermissionService.consultAllPermission();
        assertNotNull(tPermissions);
        tPermissions.forEach(System.out::println);
    }

    @Test
    public void testConsultAllPermissionByTree() {
        List<TPermissionServiceImpl.PermissionNode> list = tPermissionService.consultAllPermissionByTree();
        assertNotNull(list);
        System.out.println(list);
    }
}
