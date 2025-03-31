package com.lhl.rp.bean;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

/**
 * 角色表
 * t_role
 */
@Data
public class TRole implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 角色标识符
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述备注
     */
    private String remark;
}