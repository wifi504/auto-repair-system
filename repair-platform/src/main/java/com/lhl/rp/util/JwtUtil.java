package com.lhl.rp.util;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;

import java.util.Date;
import java.util.UUID;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/26_22:35
 */
public class JwtUtil {

    // private static final String SECRET = "repair-platform-key-d9f5fd46-1947-4588-aad5-8ca3fe9d70c2";
    private static final String SECRET = "repair-platform-key-" + UUID.randomUUID();

    public static String createToken(String username, long ttlMillis) {
        JWTSigner signer = JWTSignerUtil.hs256(SECRET.getBytes());
        return JWT.create()
                .setPayload("username", username)
                .setIssuedAt(new Date())
                .setExpiresAt(new Date(System.currentTimeMillis() + ttlMillis))
                .sign(signer);
    }

    public static String getUsername(String token) {
        return JWTUtil.parseToken(token).getPayload("username").toString();
    }

    public static boolean verify(String token) {
        return JWTUtil.verify(token, SECRET.getBytes());
    }
}
