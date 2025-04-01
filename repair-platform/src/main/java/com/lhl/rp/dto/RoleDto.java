package com.lhl.rp.dto;

import lombok.Data;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/1_15:22
 */
@Data
public class RoleDto {
    /*
    {
      "id": ,
      "orderNum": "",
      "code": ,
      "name": "",
      "remark": ""
    }
    * */

    private Long id;
    private Integer orderNum;
    private String code;
    private String name;
    private String remark;
}
