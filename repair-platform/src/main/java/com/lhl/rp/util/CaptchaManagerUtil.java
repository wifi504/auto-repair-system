package com.lhl.rp.util;

import cn.hutool.captcha.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/27_22:19
 */
public class CaptchaManagerUtil {

    // 使用 Map 存储验证码文本，key为UUID，value为验证码文本
    private static final Map<String, String> CAPTCHA_MAP = new ConcurrentHashMap<>();
    private static final int EXPIRE_TIME = 5 * 60 * 1000; // 验证码有效期：5分钟

    /**
     * 生成验证码
     *
     * @return 验证码图片的 Base64 和唯一标识符
     */
    public static Map<String, String> generateCaptcha() {
        AbstractCaptcha captcha = switch ((int) (Math.random() * 3) + 1) {
            case 1 -> CaptchaUtil.createLineCaptcha(120, 40, 4, 40);
            case 2 -> CaptchaUtil.createCircleCaptcha(120, 40, 4, 25);
            case 3 -> CaptchaUtil.createShearCaptcha(120, 40, 4, 5);
            default -> throw new IllegalStateException("Generate captcha unexpected value!");
        };


        // 生成唯一标识符
        String uuid = UUID.randomUUID().toString();
        CAPTCHA_MAP.put(uuid, captcha.getCode());

        // 5分钟后自动清除验证码
        new Thread(() -> {
            try {
                Thread.sleep(EXPIRE_TIME);
                CAPTCHA_MAP.remove(uuid);
            } catch (InterruptedException ignored) {
            }
        }).start();

        // 将验证码图片转为Base64
        String base64Image = "data:image/png;base64," + captcha.getImageBase64();

        return Map.of("uuid", uuid, "captcha", base64Image);
    }

    /**
     * 校验验证码
     *
     * @param uuid 验证码唯一标识符
     * @param code 用户输入的验证码
     * @return 是否校验成功
     */
    public static boolean verifyCaptcha(String uuid, String code) {
        if (uuid == null || code == null) return false;
        String correctCode = CAPTCHA_MAP.remove(uuid); // 验证后删除
        return correctCode != null && correctCode.equalsIgnoreCase(code);
    }
}
