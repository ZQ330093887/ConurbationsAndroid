package com.test.admin.conurbations.config;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhouqiong on 2016/3/15.
 */
public class MD5 {
    public static String encode(String content) {
        return encode(content, 32);
    }

    public static String encode(String content, int length) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(content.getBytes("utf-8"));
            String result = parseByteToHex(md5.digest());
            if (length == 32) {
                return result;
            }
            else if (length == 16) {
                return result.substring(8, 24);
            }
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseByteToHex(byte bytes[]) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < bytes.length; i ++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            stringBuilder.append(hex.toUpperCase());
        }
        return stringBuilder.toString();
    }
}
