package com.lhl.rp.dto;

import lombok.Data;

import java.util.List;

/**
 * 角色权限ID列表
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/30_14:51
 */
@Data
public class PermissionIdsDto {

    private Long roleId;
    private List<Long> idList;
}
