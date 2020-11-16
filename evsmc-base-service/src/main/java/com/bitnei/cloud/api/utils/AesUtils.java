package com.bitnei.cloud.api.utils;

import com.bitnei.cloud.sys.util.Base64;
import jodd.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public class AesUtils {

    /**
     * 自动生成AES128位密钥
     */
    @SneakyThrows
    public static String getAesCode(Integer length) {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        //要生成多少位，只需要修改这里即可128, 192或256
        kg.init(length);
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        return byteToHexString(b);
//            System.out.println(s);
//            System.out.println("十六进制密钥长度为"+s.length());
//            System.out.println("二进制密钥的长度为"+s.length()*4);
    }

    /**
     * 二进制byte[]转十六进制string
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex = Integer.toHexString(bytes[i]);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 十六进制string转二进制byte[]
     */
    public static byte[] hexStringToByte(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                System.out.println("十六进制转byte发生错误！！！");
                log.error("error", e);
            }
        }
        return baKeyword;
    }

    /**
     * aes加密
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    public static String encode(String content, String password) throws Exception {
        try {
            if(StringUtil.isEmpty(content)) {
                return "";
            }
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            //System.out.println("AES ==> " + byteArrayToHexString_sy(enCodeFormat));
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            BASE64Encoder coder = new BASE64Encoder();
            coder.encode(enCodeFormat);
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(1, key);
            byte[] result = cipher.doFinal(byteContent);
            String str = Base64.encode(result);
            return str;
        } catch (NoSuchAlgorithmException e) {
            ;
        } catch (NoSuchPaddingException e) {
            ;
        } catch (InvalidKeyException e) {
            ;
        } catch (UnsupportedEncodingException e) {
            ;
        } catch (IllegalBlockSizeException e) {
            ;
        } catch (BadPaddingException e) {
            ;
        }
        return null;
    }

    /**
     * aes解密
     * @param origin
     * @param password
     * @return
     * @throws Exception
     */
    public static String decode(String origin, String password) throws Exception {
        try {
            byte[] content = Base64.decode(origin);
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, key);
            byte[] result = cipher.doFinal(content);
            return new String(result, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            ;
        } catch (NoSuchPaddingException e) {
            ;
        } catch (InvalidKeyException e) {
            ;
        } catch (IllegalBlockSizeException e) {
            ;
        } catch (BadPaddingException e) {
            ;
        } catch (Exception e) {
            ;
        }
        return "";
    }

    @SneakyThrows
    public static void main(String[] args) {
    }
}
