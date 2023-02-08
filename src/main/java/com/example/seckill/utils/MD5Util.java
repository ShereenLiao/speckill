package com.example.seckill.utils;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {
    //front-end salt
    private static final String salt = "1a2b3c4d";
    public  static String md5(String src){
        return DigestUtils.md2Hex(src);
    }

    /**
     * first encryption: from plain text to md5 encrypted text
     **/
    public static String inputPassToFromPass(String input){
        String str = salt.charAt(0) + salt.charAt(2) + input + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * second encryption: using random salt, from first encrypted text to db stored text
     **/
    public static String fromPassToDBPass(String from, String salt){
        String str = salt.charAt(0) + salt.charAt(2) + from + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String dbsalt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = fromPassToDBPass(fromPass, dbsalt);
        return dbPass;
    }
}


