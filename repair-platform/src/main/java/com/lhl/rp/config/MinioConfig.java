package com.lhl.rp.config;

import com.lhl.rp.util.FileUtil;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * MinIO 配置类
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/26_18:34
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    // MinIO 服务地址
    private String endpoint;
    // 访问密钥
    private String accessKey;
    private String secretKey;
    // 桶
    private Bucket bucket = new Bucket();
    // 预签名URL有效期
    private int preSignedObjectUrlExpiry;

    @Data
    public static class Bucket {
        // 头像存储桶
        private String avatar;
    }

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(this.getEndpoint())
                .credentials(this.getAccessKey(), this.getSecretKey())
                .build();
        try {
            FileUtil.init(minioClient, this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return minioClient;
    }
}
