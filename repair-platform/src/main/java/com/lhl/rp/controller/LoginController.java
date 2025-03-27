package com.lhl.rp.controller;

import com.lhl.rp.bean.LoginUser;
import com.lhl.rp.config.TokenConfig;
import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import com.lhl.rp.util.CaptchaManagerUtil;
import com.lhl.rp.util.JwtUtil;
import com.lhl.rp.util.TokenCacheHolder;
import com.lhl.rp.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_0:48
 */


@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenConfig tokenConfig;

    // 认证
    @PostMapping("/login")
    public R<?> login(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");
        String uuid = user.get("uuid");
        String captcha = user.get("captcha");

        // 校验验证码
        if (!CaptchaManagerUtil.verifyCaptcha(uuid, captcha)) {
            return R.error(ResultCode.FAIL, "验证码错误或已失效");
        }

        // 执行登录认证
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authenticationManager.authenticate(token);

        // 获取用户信息
        LoginUser loginUser = (LoginUser) auth.getPrincipal();

        // 配置文件读取 Token 有效期
        long ttlMillis = tokenConfig.getTtlSecond() * 1000L;

        // 生成 JWT
        String jwt = JwtUtil.createToken(loginUser.getUsername(), ttlMillis);

        // 添加到 Token 缓存中心
        TokenCacheHolder.put(jwt, ttlMillis);

        return R.ok(jwt);
    }

    // 单 Token 注销认证
    @PostMapping("/logout")
    public R<?> logout(HttpServletRequest request) {
        String token = TokenUtil.resolveToken(request);

        if (token != null) {
            TokenCacheHolder.remove(token);
        }

        return R.ok("已成功退出登录");
    }

    // 用户所有 Token 注销认证
    @PostMapping("/logoutAll")
    public R<?> logoutAll(HttpServletRequest request) {
        String token = TokenUtil.resolveToken(request);

        if (token != null) {
            if (JwtUtil.verify(token) && TokenCacheHolder.exists(token)) {
                String username = JwtUtil.getUsername(token);
                TokenCacheHolder.removeAll(username);
                return R.ok("已成功注销用户(" + username + ")所有登录态！");
            }
        }

        TokenCacheHolder.remove(token);
        return R.error(ResultCode.UNAUTHORIZED, "Token 已失效或格式错误，无法注销登录态");
    }

    // 获取验证码
    @GetMapping("/captcha")
    public R<Map<String, String>> getCaptcha() {
        return R.ok(CaptchaManagerUtil.generateCaptcha());
    }
}
