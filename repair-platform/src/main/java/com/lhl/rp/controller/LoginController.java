package com.lhl.rp.controller;

import com.lhl.rp.bean.LoginUser;
import com.lhl.rp.config.TokenConfig;
import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import com.lhl.rp.service.TPermissionService;
import com.lhl.rp.service.TUserService;
import com.lhl.rp.util.CaptchaManagerUtil;
import com.lhl.rp.util.JwtUtil;
import com.lhl.rp.util.TokenCacheHolder;
import com.lhl.rp.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 登录相关API，此控制器所有API权限为 Public
 *
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

    @Autowired
    private TUserService tUserService;

    @Autowired
    private TPermissionService tPermissionService;

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
        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(token);
        } catch (AuthenticationException e) {
            return R.error(ResultCode.UNAUTHORIZED, "用户名或密码错误！");
        }

        // 获取用户信息
        LoginUser loginUser = (LoginUser) auth.getPrincipal();

        // 查询该用户状态是否正常
        Byte status = tUserService.selectByName(loginUser.getUsername()).getStatus();
        if (status == 0) {
            // 账户被封禁
            return R.error(ResultCode.UNAUTHORIZED, "用户被封禁！");
        } else if (status == 2) {
            // 账户不存在
            return R.error(ResultCode.UNAUTHORIZED, "用户不存在！");
        }

        // 配置文件读取 Token 有效期
        long ttlMillis = tokenConfig.getTtlSecond() * 1000L;

        // 获取用户昵称
        String nickname = loginUser.getTUser().getNickname();
        nickname = nickname == null ? loginUser.getUsername() : nickname;

        // 生成 JWT
        String jwt = JwtUtil.createToken(loginUser.getUsername(), nickname, ttlMillis);

        // 添加到 Token 缓存中心
        TokenCacheHolder.put(jwt, ttlMillis);

        // 如果是超管，获取系统所有权限
        if ("admin".equals(loginUser.getUsername())) {
            tUserService.updateUserRoles(1L, List.of(1L));
            tPermissionService.consultAllPermissionBySuperAdmin();
        }

        return R.ok(jwt);
    }

    // 单 Token 注销认证
    @PostMapping("/logout")
    public R<?> logout(HttpServletRequest request) {
        String token = TokenUtil.resolveToken(request);

        if (token != null) {
            TokenCacheHolder.remove(token);
            return R.ok(null, "已成功退出登录");
        }
        return R.error(ResultCode.UNAUTHORIZED, "无法注销登录态，令牌无效！");
    }

    // 用户所有 Token 注销认证
    @PostMapping("/logoutAll")
    public R<?> logoutAll(HttpServletRequest request) {
        String token = TokenUtil.resolveToken(request);

        if (token != null) {
            if (JwtUtil.verify(token) && TokenCacheHolder.exists(token)) {
                String username = JwtUtil.getUsername(token);
                TokenCacheHolder.removeAll(username);
                return R.ok(null, "用户(" + JwtUtil.getNickname(token) + ")已成功注销所有登录态！");
            }
        }

        TokenCacheHolder.remove(token);
        return R.error(ResultCode.UNAUTHORIZED, "无法注销登录态，令牌无效！");
    }

    // 获取验证码
    @GetMapping("/captcha")
    public R<Map<String, String>> getCaptcha() {
        return R.ok(CaptchaManagerUtil.generateCaptcha());
    }
}
