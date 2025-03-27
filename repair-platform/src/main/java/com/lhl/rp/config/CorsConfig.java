package com.lhl.rp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


/**
 * 跨域请求配置类
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_16:56
 */

@Data
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsConfig {

    // 允许哪些源访问
    private List<String> allowedOrigins = Collections.emptyList();
    // 允许哪些方法
    private List<String> allowedMethods = Collections.emptyList();
    // 允许携带哪些头部信息
    private List<String> allowedHeaders = Collections.emptyList();
    // 允许携带凭证（如果需要）
    private Boolean allowCredentials = false;
}
