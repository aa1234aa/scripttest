package com.bitnei.cloud.sys.util;

/**
 * @author DFY
 * @description 正则表达式公共类
 * @date 2018/11/1
 */
public class RegexUtil {

    /**
     * 数字或大写字母或两者都有，8-30位
     */
    public static final String NUM_OR_CAPITAL_8_30 = "^[0-9A-Z]{8,30}$";
    /**
     * 数字或大写字母或两者都有，18位
     */
    public static final String NUM_OR_CAPITAL_18_18 = "^[0-9A-Z]{18,18}$";
    /**
     * 3-30位数字或英文字母
     */
    public static final String NUM_OR_LETTER_3_30 = "^[a-zA-Z0-9]{3,30}$";

    /**
     *  C: 任意字符
     *  A: 中文英文数字
     *  E: 大小写英文
     *  N: 数字
     *  T: 特殊字符()/-
     *  M: 兼容空值
     */

    /** 2-32位中英文及数字 */
    public static final String A_2_32 = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,32}$";

    /** 2-32位中英文及数字,- */
    public static final String A_H_2_32 = "^[\\u4e00-\\u9fa5a-zA-Z0-9\\-]{2,32}$";

    /** 2-32位中英文数字及特殊字符 */
    public static final String A_T_2_32 = "^[\\u4e00-\\u9fa5a-zA-Z0-9\\(\\)\\/\\-\\_]{2,32}$";

    /** 5-30位中英文数字及特殊字符 */
    public static final String A_T_5_30 = "^[\\u4e00-\\u9fa5a-zA-Z0-9\\(\\)\\/\\-\\_]{5,30}$";

    /** 2-32位中英文数字及特殊字符 */
    public static final String A_T_0_32 = "^[\\u4e00-\\u9fa5a-zA-Z0-9\\(\\)\\/\\-\\_]{0,32}$";

    /** 0-32位中英文及数字,兼容空值 */
    public static final String A_M_2_32 = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,32}|$";

    /** 1-10位中英文及数字 */
    public static final String A_1_10 = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,10}$";

    /** 2-10位中英文及数字 */
    public static final String A_2_10 = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,10}$";

    /** 0-100位任意字符 */
    public static final String C_0_100 = "^.{0,100}$";

    /** 3-30位任意字符 */
    public static final String C_3_30 = "^.{3,30}$";

    /** 5-30位任意字符 */
    public static final String C_5_30 = "^.{5,30}$";

    /** 2-32位任意字符 */
    public static final String C_2_32 = "^.{2,32}$";

    /** 1-10位英文及数字 */
    public static final String E_M_1_10 = "^[a-zA-Z0-9]{1,10}$";

    /** 2-64位英文及数字,兼容空值 */
    public static final String E_M_2_64 = "^[a-zA-Z0-9]{2,64}|$";

    /** 0-100位任意字符,兼容空值 */
    public static final String C_M_0_100 = "^.{0,100}|$";

}
