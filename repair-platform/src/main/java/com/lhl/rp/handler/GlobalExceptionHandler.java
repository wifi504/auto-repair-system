package com.lhl.rp.handler;

import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

/**
 * 全局异常处理类
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_0:26
 */
public class GlobalExceptionHandler {

    // 未登录 Token 无效
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public R<?> handleAuthError(AuthenticationCredentialsNotFoundException e) {
        return R.error(ResultCode.UNAUTHORIZED);
    }

    // 无权限访问
    @ExceptionHandler(AccessDeniedException.class)
    public R<?> handleAccessDenied(AccessDeniedException e) {
        return R.error(ResultCode.FORBIDDEN);
    }

    // 参数校验错误（Bean校验）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleValidation(MethodArgumentNotValidException e) {
        String msg = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return R.error(ResultCode.FAIL, msg);
    }

    // 通用异常（兜底）
    @ExceptionHandler(Exception.class)
    public R<?> handleOtherErrors(Exception e, HttpServletRequest request) {
        e.printStackTrace(); // 日志可选保留
        return R.error(ResultCode.SERVER_ERROR, "系统异常：" + e.getMessage());
    }
}
