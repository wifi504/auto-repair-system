package com.lhl.rp.service;

import com.lhl.rp.service.exception.FileServiceException;
import com.lhl.rp.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/27_17:06
 */
@SpringBootTest
public class FileServiceImplTest {
    @Autowired
    private FileServiceImpl fileService;

    @Test
    public void uploadAvatar_FileIsNull_ThrowsException() {
        assertThrows(FileServiceException.class, () -> fileService.uploadAvatar(null));
    }

    @Test
    public void uploadAvatar_FileIsEmpty_ThrowsException() {
        MultipartFile file = new MockMultipartFile("emptyFile", new byte[0]);
        assertThrows(FileServiceException.class, () -> fileService.uploadAvatar(file));
    }

    @Test
    public void uploadAvatar_InvalidExtension_ThrowsException() {
        MultipartFile file = new MockMultipartFile("invalidFile", "invalidFile.txt", "text/plain", new byte[0]);
        assertThrows(FileServiceException.class, () -> fileService.uploadAvatar(file));
    }

    @Test
    public void uploadAvatar() {
        File localFile = new File("../repair-frontend/src/assets/image/banner-color-small.png");
        try (FileInputStream fis = new FileInputStream(localFile)) {
            byte[] bytes = fis.readAllBytes();
            MultipartFile file = new MockMultipartFile(localFile.getName().split("\\.")[0], localFile.getName(), "image/png", bytes);
            String url = fileService.uploadAvatar(file);
            assertNotNull(url);
            System.out.println("测试通过，图片URL为：" + url);
        } catch (Exception e) {
            fail("测试不通过：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
