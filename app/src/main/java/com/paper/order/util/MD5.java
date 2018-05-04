package com.paper.order.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zjbman
 * @Description
 * @date 2018/3/24 21:13
 **/

public class MD5 {

    public static String encode(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        byte[] result = str.getBytes();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(result);

            for (byte by : md5.digest()) {
                sb.append(String.format("%02X", by));//将生成的字节MD5值转换成字符串
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static int[] keys = new int[]{
            28, 21, 31, 49, 31, 26, 58, 53, 25, 17, 62, 53, 52, 51, 49, 54, 22, 60, 27, 23,
            61, 59, 60, 62, 53, 28, 62, 29, 22, 19, 18, 49, 63, 49, 51, 22, 60, 21, 28, 61,
            21, 31, 62, 56, 52, 25, 63, 23, 27, 59, 49, 56, 55, 52, 25, 24, 62, 21, 29, 61,
            55, 49, 61, 59, 49, 22, 55, 61, 63, 58, 50, 19, 26, 27, 18, 50, 63, 23, 49, 28,
            62, 22, 59, 56, 61, 63, 22, 17, 50, 49, 23, 61, 52, 60, 58, 17, 27, 52, 28, 51,
            21, 31, 62, 56, 52, 25, 63, 23, 27, 59, 49, 56, 55, 52, 25, 24, 62, 21, 29, 61,
            55, 49, 61, 59, 49, 22, 55, 61, 63, 58, 50, 19, 26, 27, 18, 50, 63, 23, 49, 28
    };

    public static String myEncode(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        char[] result = str.toCharArray();
        try {
            for (int i = 0; i < result.length && i < keys.length; i++) {
                char c = result[i];
                char cc = (char) (c + keys[i]);
                sb.append(cc); //将生成的字节MD5值转换成字符串
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String myDecode(String code) {
        if (code == null || "".equals(code)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        char[] result = code.toCharArray();
        try {
            for (int i = 0; i < result.length && i < keys.length; i++) {
                char c = result[i];
                char cc = (char) (c - keys[i]);
                sb.append(cc); //将生成的字节MD5值转换成字符串
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * 获得32位小写的md5
     *
     * @param str
     * @return
     */
    public static String get32MD5(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }

        MessageDigest md5;
        String result = "";
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update((str).getBytes("UTF-8"));
            byte b[] = md5.digest();
            int i;
            StringBuffer buf = new StringBuffer("");

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
                result = buf.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过MD5进行散列
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String md5(String data) {
        MessageDigest md = null;
        StringBuffer buf = new StringBuffer();
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(data.getBytes());
        byte[] bits = md.digest();

        addBits(buf, bits);

        return buf.toString();
    }

    /**
     * 通过SHA1进行散列
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String sha1(String data) {
        MessageDigest md = null;
        StringBuffer buf = new StringBuffer();
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        md.update(data.getBytes());
        byte[] bits = md.digest();

        addBits(buf, bits);

        return buf.toString();
    }

    private static void addBits(StringBuffer buf, byte[] bits) {
        for (int i = 0; i < bits.length; i++) {
            int a = bits[i];
            if (a < 0) a += 256;
            if (a < 16) buf.append("0");
            buf.append(Integer.toHexString(a));
        }
    }

    public static void main(String[] args) {
        String str = "http://www.adview.cn/";
        String aa = MD5.get32MD5(str);
        System.out.println(aa);
    }
}
