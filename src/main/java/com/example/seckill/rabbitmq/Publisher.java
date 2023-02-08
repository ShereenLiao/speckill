package com.example.seckill.rabbitmq;

import com.example.seckill.dto.SeckillMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class Publisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();
    public void sendSeckillMessage(SeckillMessage message) throws JsonProcessingException {
//        String strMessage = objectMapper.writeValueAsString(message);
        log.info("发送消息" + message.toString());
        rabbitTemplate.convertAndSend("seckillExchange", "seckill.message", message);
    }

}
