package com.lhl.rp.controller;

import com.lhl.rp.bean.LoginUser;
import com.lhl.rp.result.R;
import com.lhl.rp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public R<?> login(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");

        // 执行登录认证
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authenticationManager.authenticate(token);

        // 获取用户信息
        LoginUser loginUser = (LoginUser) auth.getPrincipal();

        // 生成 JWT
        String jwt = JwtUtil.createToken(loginUser.getUsername());

        return R.ok(jwt);
    }
}
