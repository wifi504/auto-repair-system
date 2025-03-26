package com.lhl.rp.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 权限不足处理器（禁止访问）
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_0:34
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(
                R.error(ResultCode.FORBIDDEN)
        ));
    }
}
