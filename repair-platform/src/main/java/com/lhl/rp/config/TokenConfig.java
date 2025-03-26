package com.lhl.rp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_2:04
 */

@Data
@Component
@ConfigurationProperties(prefix = "token")
public class TokenConfig {

    private long cleanupIntervalSecond; // 清理周期（秒）
    private long ttlSecond; // Token 有效时间（秒）
}
