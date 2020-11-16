package com.bitnei.cloud.sys.util;

import java.security.SecureRandom;

/**
 * 随机数生成工具
 *
 * @author zhouxianzhou
 */
public class RandomUtil {

    private static SecureRandom random = new SecureRandom();


    public static void main(String[] args) {
        System.out.println(generateRandom("利",0));
    }

    /**
     * 随机生成 前缀 + [0-9][A-Z]的随机字符串
     *
     * @param prefix 前缀
     * @param length 长度
     * @return 字符串
     */
    public static String generateRandom(String prefix, int length) {

        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < length; i++) {
            // 随机选择生成数字或大写字母
            int index = random.nextInt(2);
            switch (index) {
                case 0:
                    // 随机生成0-9的数字
                    int number = random.nextInt(10);
                    sb.append(number);
                    break;
                case 1:
                    // 随机生成65-90的数字
                    // A-Z的ASCII为65-90
                    int letter = random.nextInt(26) + 65;
                    sb.append((char) letter);
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 随机生成 前缀 + [0-9]的随机字符串
     *
     * @param prefix 前缀
     * @param length 长度
     * @return 字符串
     */
    public static String generateRandomNum(String prefix, int length) {
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(10);
            sb.append(number);
        }
        return sb.toString();
    }

}
