package com.lhl.rp.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Token 工具类
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_3:52
 */
public class TokenUtil {
    /**
     * 从 http 请求体中解析 token
     *
     * @param request http 请求体
     * @return token
     */
    public static String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
