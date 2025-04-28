package com.lhl.rp.service.impl;

import com.lhl.rp.config.MinioConfig;
import com.lhl.rp.service.FileService;
import com.lhl.rp.service.exception.FileServiceException;
import com.lhl.rp.util.FileUtil;
import com.lhl.rp.util.ImageUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/27_16:41
 */
@Service
public class FileServiceImpl implements FileService {

    // 从 MinioConfig 获取信息
    @Autowired
    private MinioConfig minioConfig;

    private String avatarBucket;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        avatarBucket = minioConfig.getBucket().getAvatar();
    }

    /**
     * 上传头像
     *
     * @param file 上传的文件
     * @return 文件访问URL
     */
    @Override
    public String uploadAvatar(MultipartFile file) throws FileServiceException {
        if (file == null || file.isEmpty()) {
            throw new FileServiceException("上传文件不能为空");
        }
        // 头像格式必须为 jpg、jpeg、png、gif
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !originalFilename.toLowerCase().matches("^.+\\.(jpg|jpeg|png|gif)$")) {
            throw new FileServiceException("头像格式必须为 jpg、jpeg、png、gif");
        }
        // 图片大小限制在2M以内
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new FileServiceException("图片大小不能超过2M");
        }
        // 压缩图片尺寸至 256x256
        MultipartFile resizeFile = ImageUtil.resetSize(file, 256, 256);
        // 上传
        try {
            return FileUtil.uploadMultipartFile(resizeFile, avatarBucket);
        } catch (Exception e) {
            throw new FileServiceException(e.getMessage());
        }
    }

    /**
     * 获取头像的预签名链接
     *
     * @param fileName 头像文件名
     * @return 预签名链接
     */
    @Override
    public String getAvatarUrl(String fileName) throws FileServiceException {
        try {
            if (fileName == null || fileName.isEmpty()) {
                throw new FileServiceException("文件名不能为空");
            }
            return FileUtil.getFileUrl(avatarBucket, fileName);
        } catch (Exception e) {
            throw new FileServiceException(e.getMessage());
        }
    }
}
