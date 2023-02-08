package com.example.seckill.utils;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MD5UtilTest {

    @Test
    public void md5UtilTest(){
        String pass = "123456";
        String salt = "1a2b3c4d";
        String firstEncryptedPass = MD5Util.inputPassToFromPass(pass);
        String secondEncryptedPass = MD5Util.fromPassToDBPass(firstEncryptedPass, salt);
        String dbPass = MD5Util.inputPassToDBPass(pass, salt);
        Assert.assertEquals(dbPass, secondEncryptedPass);
    }
}
