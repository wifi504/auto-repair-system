package com.lhl.rp.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhl.rp.bean.TRole;
import com.lhl.rp.dto.RoleDto;
import com.lhl.rp.result.R;
import com.lhl.rp.service.TRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public R<PageInfo<TRole>> view(@RequestParam(defaultValue = "1") Integer pageNo,
                                   @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageHelper.startPage(pageNo, pageSize);
        List<TRole> tRoles = tRoleService.consultAllRole();
        PageHelper.clearPage();
        return R.ok(new PageInfo<>(tRoles));
    }

    @PutMapping("/update")
    public R<?> update(@RequestBody RoleDto roleDto) {
        TRole tRole = tRoleService.consultById(roleDto.getId());
        tRole.setName(roleDto.getName());
        tRole.setCode(roleDto.getCode());
        tRole.setRemark(roleDto.getRemark());
        tRole.setOrderNum(roleDto.getOrderNum());
        return R.ok(null, "成功更新" + tRoleService.updateById(tRole) + "条数据！");
    }

    @DeleteMapping("/delete")
    public R<?> delete(@RequestBody List<RoleDto> roleDtoList) {
        int count = -1;
        if (roleDtoList.size() == 1) {
            count = tRoleService.deleteById(roleDtoList.get(0).getId());
        } else {
            // 批量删除
            List<Long> ids = new ArrayList<>();
            roleDtoList.forEach(roleDto -> ids.add(roleDto.getId()));
            count = tRoleService.deleteByIds(ids);
        }
        return R.ok(null, "成功删除" + count + "条数据！");
    }


    @PostMapping("/create")
    public R<?> create(@RequestBody RoleDto roleDto) {
        TRole newRole = TRole.builder()
                .name(roleDto.getName())
                .code(roleDto.getCode())
                .remark(roleDto.getRemark())
                .orderNum(roleDto.getOrderNum())
                .build();
        return R.ok(null, "成功创建" + tRoleService.creatRole(newRole) + "条数据！");
    }
}