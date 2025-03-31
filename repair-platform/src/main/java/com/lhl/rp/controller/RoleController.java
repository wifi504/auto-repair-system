package com.lhl.rp.controller;

import com.lhl.rp.bean.TRole;
import com.lhl.rp.result.R;
import com.lhl.rp.service.TRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/31_11:50
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    TRoleService tRoleService;

    /**
     * 查询角色
     */
    @GetMapping("/view")
    public R<List<TRole>> view() {
        return R.ok(tRoleService.consultAllRole());
    }
}