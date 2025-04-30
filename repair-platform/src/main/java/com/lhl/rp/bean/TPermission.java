package com.lhl.rp.bean;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

/**
 * 权限表
 * t_permission
 */
@Data
public class TPermission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    private Long id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 父权限ID
     */
    private Long parentId;

    /**
     * 权限标识符
     */
    private String code;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 权限类型：list=目录, menu=菜单，api=接口
     */
    private Object type;

    /**
     * 权限对应的路径（接口或菜单路径）
     */
    private String url;

    /**
     * 菜单图标
     */
    private String icon;
}