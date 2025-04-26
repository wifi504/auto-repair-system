package com.lhl.rp.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MinIO 配置类测试
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/26_18:34
 */
@SpringBootTest
public class MinioConfigTest {

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient minioClient;

    @Test
    public void testConfigPropertiesLoaded() {
        // 验证配置属性是否正确加载
        assertNotNull(minioConfig.getEndpoint());
        assertNotNull(minioConfig.getAccessKey());
        assertNotNull(minioConfig.getSecretKey());
        assertNotNull(minioConfig.getBucket());
        assertNotNull(minioConfig.getBucket().getAvatar());

        System.out.println("MinIO配置测试通过:");
        System.out.println("Endpoint: " + minioConfig.getEndpoint());
        System.out.println("AccessKey: " + minioConfig.getAccessKey());
        System.out.println("Bucket Avatar: " + minioConfig.getBucket().getAvatar());
    }

    @Test
    public void testMinioClientBeanCreated() {
        // 验证MinioClient Bean是否成功创建
        assertNotNull(minioClient);
        System.out.println("MinioClient Bean创建测试通过" + minioClient);
    }

    @Test
    public void testMinioConnection() throws Exception {
        // 测试是否能成功连接到MinIO服务器
        assertDoesNotThrow(() -> {
            // 尝试列出buckets来验证连接
            List<Bucket> buckets = minioClient.listBuckets();
            System.out.println("MinIO连接测试通过：");
            buckets.forEach(bucket -> {
                System.out.println(bucket.name() + " --- " + bucket.creationDate());
            });
        });
    }

    @Test
    public void testBucketOperations() throws Exception {
        String bucketName = minioConfig.getBucket().getAvatar();

        // 检查bucket是否存在
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build());

        if (!exists) {
            // 如果不存在则创建
            System.out.println("不存在，创建 Bucket");
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }

        assertTrue(minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build()));

        System.out.println("Bucket操作测试通过: " + bucketName);
    }
}
