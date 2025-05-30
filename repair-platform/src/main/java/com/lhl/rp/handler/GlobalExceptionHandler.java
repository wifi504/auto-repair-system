package com.lhl.rp.handler;

import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

/**
 * 全局异常处理类
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_0:26
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 未登录 Token 无效
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public R<?> handleAuthError(AuthenticationCredentialsNotFoundException e) {
        return R.error(ResultCode.UNAUTHORIZED);
    }

    // 处理请求方法异常
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> handleMethodNotSupported() {
        return R.error(ResultCode.METHOD_NOT_ALLOWED);
    }

    // 处理404异常
    @ExceptionHandler(NoResourceFoundException.class)
    public R<?> handleNotFound(HttpServletRequest request) {
        return R.error(ResultCode.NOT_FOUND);
    }

    // 无权限访问
    @ExceptionHandler(AccessDeniedException.class)
    public R<?> handleAccessDenied(AccessDeniedException e) {
        return R.error(ResultCode.FORBIDDEN);
    }

    // 方法参数类型不匹配异常
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        return R.error(ResultCode.FORBIDDEN, "方法参数异常");
    }

    // 参数校验错误（Bean校验）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleValidation(MethodArgumentNotValidException e) {
        String msg = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return R.error(ResultCode.FAIL, msg);
    }

    // JSON 转换 Dto 错误（JSON数据不合法）
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<?> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return R.error(ResultCode.FAIL, "请求数据不合法：" + e.getMessage().split(":")[0]);
    }

    // 通用异常（兜底）
    @ExceptionHandler(Exception.class)
    public R<?> handleOtherErrors(Exception e, HttpServletRequest request) {
        e.printStackTrace(); // 日志可选保留
        return R.error(ResultCode.SERVER_ERROR, "系统异常：" + e.getMessage().split(":")[0]);
    }
}
