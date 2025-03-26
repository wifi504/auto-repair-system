package com.lhl.rp.result;

import lombok.Getter;

/**
 * 状态码枚举类
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_0:22
 */

@Getter
public enum ResultCode {

    SUCCESS(200, "success"),
    FAIL(400, "fail"),
    UNAUTHORIZED(401, "未登录或 token 无效"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    SERVER_ERROR(500, "服务器内部错误");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
