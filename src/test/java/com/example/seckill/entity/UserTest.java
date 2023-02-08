package com.example.seckill.entity;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class UserTest {
    @Test
    public void createUser(){
        User user = new User();
    }
}
