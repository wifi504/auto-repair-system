package com.lhl.rp.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证失败处理器（未登录）
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_0:32
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(
                R.error(ResultCode.UNAUTHORIZED)
        ));
    }
}
