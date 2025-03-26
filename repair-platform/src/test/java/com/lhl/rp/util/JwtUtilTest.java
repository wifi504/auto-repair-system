package com.lhl.rp.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/26_23:06
 */
public class JwtUtilTest {

    @Test
    public void testCreateToken() {
        String token = JwtUtil.createToken("admin");
        assertNotNull(token);
        System.out.println("Token: " + token);
    }

    @Test
    public void testGetUsername() {
        String token = JwtUtil.createToken("admin");
        String username = JwtUtil.getUsername(token);
        assertEquals("admin", username);
    }

    @Test
    public void testVerify() {
        String token = JwtUtil.createToken("admin");
        assertTrue(JwtUtil.verify(token));
    }
}
