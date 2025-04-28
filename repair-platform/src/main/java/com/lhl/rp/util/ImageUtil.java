package com.lhl.rp.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

/**
 * 图片处理工具类
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/28_21:07
 */
public class ImageUtil {

    /**
     * 压缩图片尺寸
     *
     * @param file   图片文件
     * @param width  宽
     * @param height 高
     * @return 压缩后的 MultipartFile
     */
    public static MultipartFile resetSize(MultipartFile file, int width, int height) {
        try {
            if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
                throw new RuntimeException("图片不能为空");
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            String[] split = file.getOriginalFilename().split("\\.");
            String formatName = split[split.length - 1];
            Thumbnails.of(file.getInputStream())
                    .size(width, height)
                    .keepAspectRatio(false) // 设为true则保持宽高比
                    .outputFormat(formatName)
                    .toOutputStream(outputStream);
            return new CustomMultipartFile(
                    file.getName(),
                    file.getOriginalFilename(),
                    file.getContentType(),
                    outputStream.toByteArray()
            );
        } catch (Exception e) {
            throw new RuntimeException("图片处理失败：" + e.getMessage());
        }
    }
}
