package com.lhl.rp.service;

import com.lhl.rp.service.exception.FileServiceException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/27_0:51
 */
public interface FileService {

    /**
     * 上传头像
     *
     * @param file 上传的文件
     * @return 文件访问URL
     */
    String uploadAvatar(MultipartFile file) throws FileServiceException;

    /**
     * 获取头像的预签名链接
     *
     * @param fileName 头像文件名
     * @return 预签名链接
     */
    String getAvatarUrl(String fileName) throws FileServiceException;
}
