package com.lhl.rp.controller;

import com.lhl.rp.result.R;
import com.lhl.rp.result.ResultCode;
import com.lhl.rp.service.FileService;
import com.lhl.rp.service.exception.FileServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/27_0:50
 */

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload-avatars")
    public R<?> uploadAvatars(@RequestParam("file") MultipartFile file) {
        try {
            return R.ok(fileService.uploadAvatar(file));
        } catch (FileServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }

    @GetMapping("/get-avatars-url/{avatarName}")
    public R<?> getAvatarsUrl(@PathVariable String avatarName) {
        try {
            return R.ok(fileService.getAvatarUrl(avatarName));
        } catch (FileServiceException e) {
            return R.error(ResultCode.FAIL, e.getMessage());
        }
    }
}
