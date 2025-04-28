package com.lhl.rp.util;

import com.lhl.rp.config.MinioConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/27_20:33
 */
@SpringBootTest
public class FileUtilTest {

    @Autowired
    private MinioConfig minioConfig;

    @Test
    public void testGetFileUrl() {
        String fileUrl = FileUtil.getFileUrl(minioConfig.getBucket().getAvatar(), "a");
        assertNull(fileUrl);
        System.out.println(fileUrl);
    }
}
