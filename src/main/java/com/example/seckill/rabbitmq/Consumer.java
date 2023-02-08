package com.example.seckill.rabbitmq;

import com.example.seckill.dto.SeckillMessage;
import com.example.seckill.entity.VoucherOrder;
import com.example.seckill.service.VoucherOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class Consumer {
    @Resource
    private VoucherOrderService voucherOrderService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "seckillQueue")
    public void receive(SeckillMessage seckillMessage) throws JsonProcessingException {
//        log.info("接收消息：" + message);
//        SeckillMessage seckillMessage = objectMapper.readValue(message, SeckillMessage.class);

        Long voucherId = seckillMessage.getVoucherId();
        Long userId = seckillMessage.getUserId();
        Long orderId = seckillMessage.getOrderId();

        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setVoucherId(voucherId);
        voucherOrder.setUserId(userId);
        voucherOrder.setId(orderId);

        voucherOrderService.updateVoucherDatabase(voucherOrder);
    }


}
