package com.example.seckill.service.strategy;

import com.example.seckill.dto.Result;
import com.example.seckill.dto.SeckillMessage;
import com.example.seckill.rabbitmq.Publisher;
import com.example.seckill.utils.RedisIdWorker;
import com.example.seckill.utils.UserHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

@Slf4j
@Component
public class VoucherOrderLuaRabbitMqStrategy {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private Publisher publisher;
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    public Result seckillVoucher(Long voucherId) throws JsonProcessingException {
        Long userId = UserHolder.getUser().getId();
        long orderId = redisIdWorker.nextId("order");
        // 1.执行lua脚本
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(), userId.toString(), String.valueOf(orderId)
        );
        int r = result.intValue();
        // 2.判断结果是否为0
        if (r != 0) {
            // 2.1.不为0 ，代表没有购买资格
            return Result.fail(r == 1 ? "库存不足" : "不能重复下单");
        }
        // 3.返回订单id
        //todo: create message, put into mq
        SeckillMessage message = new SeckillMessage();
        message.setVoucherId(voucherId);
        message.setUserId(userId);
        message.setOrderId(orderId);
        publisher.sendSeckillMessage(message);
        return Result.ok(orderId);
    }



}
