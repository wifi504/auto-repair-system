package com.lhl.rp.util;

import com.lhl.rp.config.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 文件操作工具类
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/27_16:00
 */
public class FileUtil {

    private static MinioClient minioClient;
    private static int expiry;

    /**
     * 初始化工具类（需要在项目启动时调用）
     */
    public static void init(MinioClient client, MinioConfig minioConfig) {
        minioClient = client;
        expiry = minioConfig.getPreSignedObjectUrlExpiry();

        // 利用反射机制遍历所有存储桶并确认其存在
        Class<? extends MinioConfig.Bucket> bucketClazz = minioConfig.getBucket().getClass();
        for (java.lang.reflect.Method method : bucketClazz.getDeclaredMethods()) {
            if (method.getName().contains("get")) {
                try {
                    String bucketName = (String) method.invoke(minioConfig.getBucket());
                    if (!minioClient.bucketExists(BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build())) {
                        minioClient.makeBucket(MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("获取存储桶失败：" + e.getMessage());
                }
            }
        }
    }

    /**
     * 通用 MultipartFile 文件上传方法
     */
    public static String uploadMultipartFile(MultipartFile file, String bucketName) {
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("上传文件不能为空");
            }
            // 验证 BucketName
            if (bucketName == null || bucketName.isEmpty()) {
                throw new IllegalArgumentException("BucketName 不能为空");
            }
            if (!minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build())) {
                throw new IllegalArgumentException("Bucket 不存在");
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = generateObjectName(extension);

            // 上传文件
            return uploadInputStream(file.getInputStream(), file.getSize(), file.getContentType(), bucketName, objectName);
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 通用 InputStream 文件上传方法
     */
    public static String uploadInputStream(InputStream inputStream, long fileSize, String contentType, String bucketName, String objectName) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, fileSize, -1)
                            .contentType(contentType)
                            .build());
            // 返回访问URL
            return getFileUrl(bucketName, objectName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取文件访问URL
     */
    public static String getFileUrl(String bucketName, String objectName) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expiry, TimeUnit.SECONDS)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("获取文件访问URL失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("删除文件失败: " + e.getMessage());
        }
    }

    /**
     * 生成唯一对象名
     */
    private static String generateObjectName(String extension) {
        return UUID.randomUUID() + extension;
    }
}
