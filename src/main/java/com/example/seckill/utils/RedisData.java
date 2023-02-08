package com.example.seckill.utils;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedisData {
    // LocalDateTime ： 同时含有年月日时分秒的日期对象,thread-safe
    private LocalDateTime expireTime;
    private Object data;
}
