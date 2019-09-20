package com.len.task.common.util;

import java.util.Random;

/**
 * 随机数相关的一个工具类
 *
 * @author pk
 * @since 2017/9/26.
 */
public class RandomUtil {
    private RandomUtil() {
    }

    private static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String DIGITAL_CHAR = "0123456789";

    /**
     * 生成一个随机字符串
     * 目前用于生成In模块中的cpkey
     *
     * @param length 长度
     * @return 随机字符串
     */
    public static String generateString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));
        }
        return sb.toString();
    }

    public static String generateVerificationCode() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(DIGITAL_CHAR.charAt(random.nextInt(DIGITAL_CHAR.length())));
        }
        return sb.toString();
    }

    public static String generateTransactionId() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 18; i++) {
            sb.append(DIGITAL_CHAR.charAt(random.nextInt(DIGITAL_CHAR.length())));
        }
        return sb.toString();
    }
}
