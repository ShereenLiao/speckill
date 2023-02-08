package com.example.seckill.utils;


import com.example.seckill.SeckillApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = {SeckillApplication.class})
@RunWith(SpringRunner.class)
public class RedisIdWorkerTest {
    @Resource
    private RedisIdWorker redisIdWorker;

    @Test
    public void testRandomId(){
        long id = redisIdWorker.nextId("order");
        System.out.println(id);
    }
}
