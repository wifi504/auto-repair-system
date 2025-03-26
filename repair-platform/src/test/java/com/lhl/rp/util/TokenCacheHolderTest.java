package com.lhl.rp.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Token 缓存中心 单元测试
 * （Redis的廉价平替）
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_2:16
 */

class TokenCacheHolderTest {

    private static final String USER = "test-user";

    @BeforeEach
    void setup() {
        // 清理全部测试 token（模拟简单清理）
        TokenCacheHolder.removeAll(USER);
        TokenCacheHolder.remove("fake-token");
    }

    @Test
    void testPutAndExists() {
        String token = JwtUtil.createToken(USER, 2000);
        TokenCacheHolder.put(token, 2000);
        assertTrue(TokenCacheHolder.exists(token));
    }

    @Test
    void testTokenExpires() throws InterruptedException {
        String token = JwtUtil.createToken(USER, 1000);
        TokenCacheHolder.put(token, 1000);
        Thread.sleep(1100);
        assertFalse(TokenCacheHolder.exists(token));
    }

    @Test
    void testRemoveToken() {
        String token = JwtUtil.createToken(USER, 5000);
        TokenCacheHolder.put(token, 5000);
        TokenCacheHolder.remove(token);
        assertFalse(TokenCacheHolder.exists(token));
    }

    @Test
    void testClearExpiredTokens() throws Exception {
        String token1 = JwtUtil.createToken(USER, 1000);
        String token2 = JwtUtil.createToken(USER, 10000);
        TokenCacheHolder.put(token1, 1000);
        TokenCacheHolder.put(token2, 10000);

        Thread.sleep(1200);

        Method clear = TokenCacheHolder.class.getDeclaredMethod("clearExpiredTokens");
        clear.setAccessible(true);
        clear.invoke(new TokenCacheHolder(null));

        assertFalse(TokenCacheHolder.exists(token1));
        assertTrue(TokenCacheHolder.exists(token2));
    }

    @Test
    void testRemoveAllForUser() {
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String token = JwtUtil.createToken(USER, 5000);
            tokens.add(token);
            TokenCacheHolder.put(token, 5000);
        }

        // 所有 token 均存在
        tokens.forEach(token -> assertTrue(TokenCacheHolder.exists(token)));

        // 一次性移除
        TokenCacheHolder.removeAll(USER);

        tokens.forEach(token -> assertFalse(TokenCacheHolder.exists(token)));
    }

    @Test
    void testRemoveAllNullUser() {
        // 应该不会抛异常
        assertDoesNotThrow(() -> TokenCacheHolder.removeAll(null));
    }
}
