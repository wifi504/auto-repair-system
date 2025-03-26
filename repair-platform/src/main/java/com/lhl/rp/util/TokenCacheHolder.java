package com.lhl.rp.util;

import com.lhl.rp.config.TokenConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token 缓存中心
 * （Redis的廉价平替）
 * <br />
 * 临时内存版 Token 缓存，适用于小规模环境
 * 大规模上线迁移至 Redis + TTL 管控！
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_1:59
 */
@Component
@RequiredArgsConstructor
public class TokenCacheHolder {

    // 让 Spring 容器自动注入我们的自定义Config，使定时任务注解可以读取到 Spring 外部配置文件
    private final TokenConfig tokenConfig;

    // token -> 过期时间
    private static final Map<String, Long> tokenMap = new ConcurrentHashMap<>();

    /**
     * 在 Token 缓存中心存放 Token 并指定有效期
     *
     * @param token     Token
     * @param ttlMillis 有效期（毫秒）
     */
    public static void put(String token, long ttlMillis) {
        tokenMap.put(token, System.currentTimeMillis() + ttlMillis);

    }

    /**
     * 判断指定 Token 是否有效
     *
     * @param token Token
     * @return Boolean
     */
    public static boolean exists(String token) {
        Long expireAt = tokenMap.get(token);
        return expireAt != null && expireAt > System.currentTimeMillis();
    }

    /**
     * 从 Token 缓存中心移除指定 Token
     *
     * @param token Token
     */
    public static void remove(String token) {
        tokenMap.remove(token);
    }

    /**
     * 从 Token 缓存中心移除指定用户所有签发 Token
     *
     * @param username 用户登录名
     */
    public static void removeAll(String username) {
        if (username == null) return;

        List<String> toRemove = new ArrayList<>();
        for (String token : tokenMap.keySet()) {
            try {
                if (JwtUtil.getUsername(token).equals(username)) {
                    toRemove.add(token);
                }
            } catch (Exception e) {
                // token 异常，跳过
            }
        }

        toRemove.forEach(tokenMap::remove);
    }

    // 每隔固定时间自动清理过期 token
    @Scheduled(fixedDelayString = "#{@tokenConfig.cleanupIntervalSecond * 1000}")
    public void clearExpiredTokens() {
        long now = System.currentTimeMillis();
        tokenMap.entrySet().removeIf(stringLongEntry -> stringLongEntry.getValue() <= now);
    }
}
