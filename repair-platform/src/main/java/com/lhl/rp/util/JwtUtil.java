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

    /**
     * 创建携带用户登录名的 JWT
     *
     * @param username  登录名
     * @param ttlMillis 有效期
     * @return JWT
     */
    public static String createToken(String username, long ttlMillis) {
        JWTSigner signer = JWTSignerUtil.hs256(SECRET.getBytes());
        return JWT.create()
                .setPayload("username", username)
                .setIssuedAt(new Date())
                .setExpiresAt(new Date(System.currentTimeMillis() + ttlMillis))
                .sign(signer);
    }

    /**
     * 创建携带用户登录名和用户昵称的 JWT
     *
     * @return JWT
     */
    public static String createToken(String username, String nickname, long ttlMillis) {
        JWTSigner signer = JWTSignerUtil.hs256(SECRET.getBytes());
        return JWT.create()
                .setPayload("username", username)
                .setPayload("nickname", nickname)
                .setIssuedAt(new Date())
                .setExpiresAt(new Date(System.currentTimeMillis() + ttlMillis))
                .sign(signer);
    }

    public static String getUsername(String token) {
        return JWTUtil.parseToken(token).getPayload("username").toString();
    }

    public static String getNickname(String token) {
        try {
            return JWTUtil.parseToken(token).getPayload("nickname").toString();
        } catch (Exception e) {
            return getUsername(token);
        }
    }

    public static boolean verify(String token) {
        try {
            return JWTUtil.verify(token, SECRET.getBytes());
        } catch (Exception e) {
            return false;
        }
    }
}
