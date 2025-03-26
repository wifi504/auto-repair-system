package com.lhl.rp.util;

import com.lhl.rp.config.TokenConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/26_23:06
 */
@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private TokenConfig tokenConfig;

    private long ttlMillis;

    @BeforeEach
    public void before() {
        ttlMillis = tokenConfig.getTtlSecond() * 1000L;
    }

    @Test
    public void testCreateToken() {
        String token = JwtUtil.createToken("admin", ttlMillis);
        assertNotNull(token);
        System.out.println("Token: " + token);
    }

    @Test
    public void testGetUsername() {
        String token = JwtUtil.createToken("admin", ttlMillis);
        String username = JwtUtil.getUsername(token);
        assertEquals("admin", username);
    }

    @Test
    public void testVerify() {
        String token = JwtUtil.createToken("admin", ttlMillis);
        assertTrue(JwtUtil.verify(token));
    }
}
