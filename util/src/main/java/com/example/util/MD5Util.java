package com.example.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MD5Util {
    private static final String TAG = "MD5Utils";

    //获取MD5编码运算结果
    public static String getMD5_1(String sourceStr) {
        char[] hexDigits = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(sourceStr.getBytes(StandardCharsets.UTF_8));
            char[] str = new char[hash.length * 2];
            int k = 0;
            for (byte b : hash) {
                str[k++] = hexDigits[b >>> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            result = new String(str).toLowerCase(Locale.getDefault());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "getMD5_1: e = " + e.getMessage());
        }
        return result;
    }

    //获取MD5编码运算结果
    public static String getMD5_2(String sourceStr) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(sourceStr.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "getMD5_2: e = " + e.getMessage());
            return sourceStr;
        }
    }

    public static String fileMD5(File file) {
        FileInputStream fis = null;
        try {
            byte[] hash;
            byte[] buffer = new byte[4096];
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            int len;
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            hash = md.digest();
            //对生成的16字节数组进行补零操作
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String getFileMD5(File file) {
        FileInputStream fis = null;
        try {
            byte[] buffer = new byte[4096];
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            int len;
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, md.digest());
            String md5 = bigInt.toString(16);
            if (md5.length() != 32) {
                StringBuilder temp = new StringBuilder();
                for (int i = md5.length(); i < 32; i++) {
                    temp.append("0");
                }
                md5 = temp + md5;
            }
            return md5;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
