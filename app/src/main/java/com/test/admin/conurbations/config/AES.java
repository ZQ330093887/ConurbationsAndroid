package com.test.admin.conurbations.config;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zhouqiong on 2016/3/15.
 */
public class AES {

    private static final String TAG = "AES";

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final String DEFAULT_PASSWORD = "youqianbang";

    private static KeyGenerator keyGenerator;

    private static Cipher cipher;

    private static boolean isInit = false;

    private static void init() {
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGenerator.init(128);
        try {
            cipher = Cipher.getInstance(ALGORITHM);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        isInit = true;
    }

    private static byte[] generateKey() {
        if (!isInit) {
            init();
        }
        return keyGenerator.generateKey().getEncoded();
    }

    private static byte[] encrypt(byte[] content, byte[] keyBytes) {
        byte[] encryptedText = null;
        if (!isInit) {
            init();
        }

        Key key = new SecretKeySpec(keyBytes, "AES");
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            encryptedText = cipher.doFinal(content);
        }
        catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return encryptedText;
    }

    private static byte[] encrypt(String content, String password) {
        try {
            byte[] keyBytes = getKey(password);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(byteContent);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] decrypt(byte[] content, String password) {
        try {
            byte[] keyBytes = getKey(password);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(content);
        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getKey(String password) {
        byte[] bytes;
        if (password != null) {
            bytes = password.getBytes();
        }
        else {
            bytes = new byte[24];
        }
        return bytes;
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

    public static byte[] parseHexToByte(String hexString) {
        if (hexString.length() < 1) {
            return null;
        }

        byte[] result = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length() / 2; i ++) {
            int high = Integer.parseInt(hexString.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexString.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static String encode(String content) {
        return encode(content, DEFAULT_PASSWORD);
    }

    public static String decode(String content) {
        return decode(content, DEFAULT_PASSWORD);
    }

    public static String encode(String content, String password) {
        return parseByteToHex(encrypt(content, MD5.encode(password, 16)));
    }

    public static String decode(String content, String password) {
        byte[] bytes = decrypt(parseHexToByte(content), MD5.encode(password, 16));

        if(bytes == null) {
            return null;
        }
        return new String(bytes);
    }
}

