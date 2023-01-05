package org.edward.blackbox.util;

public class DataUtil {
    public static String toHexString(byte b) {
        String hex = Integer.toHexString(b&0xFF);
        if(hex.length() == 1) {
            return '0'+hex;
        }
        return hex;
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<bytes.length; i++) {
            sb.append(toHexString(bytes[i]));
        }
        return sb.toString();
    }

    public static String toHexString(char c) {
        return Integer.toHexString(c);
    }

    public static String toHexString(char[] chars) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<chars.length; i++) {
            sb.append(toHexString(chars[i]));
        }
        return sb.toString();
    }

    public static String[] toHexStringArray(byte[] bytes) {
        String[] hexArray = new String[bytes.length];
        for(int i=0; i<bytes.length; i++) {
            hexArray[i] = toHexString(bytes[i]);
        }
        return hexArray;
    }

    public static String[] toHexStringArray(char[] chars) {
        String[] hexArray = new String[chars.length];
        for(int i=0; i<chars.length; i++) {
            hexArray[i] = toHexString(chars[i]);
        }
        return hexArray;
    }

    public static void main(String[] args) {
        byte cc = 0x00;
        System.out.println(toHexString(cc));
        char dd = '$';
        System.out.println(toHexString(dd));
    }
}