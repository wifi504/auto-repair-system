package com.lhl.rp.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户角色ID列表
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/29_1:14
 */
@Data
public class RoleIdsDto {

    private Long userId;
    private List<Long> idList;
}
