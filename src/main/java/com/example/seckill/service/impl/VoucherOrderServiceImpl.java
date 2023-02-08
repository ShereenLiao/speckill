package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.dto.Result;
import com.example.seckill.entity.VoucherOrder;
import com.example.seckill.mapper.VoucherOrderMapper;
import com.example.seckill.service.SeckillVoucherService;
import com.example.seckill.service.VoucherOrderService;
import com.example.seckill.service.strategy.VoucherOrderLuaRabbitMqStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements VoucherOrderService {
    @Resource
    private SeckillVoucherService seckillVoucherService;
    @Resource
    private VoucherOrderLuaRabbitMqStrategy luaRabbitMqStrategy;

    @Override
    public Result seckillVoucher(Long voucherId) throws JsonProcessingException {
        //lua + rabbitmq strategy
        return luaRabbitMqStrategy.seckillVoucher(voucherId);
    }

    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public void updateVoucherDatabase(VoucherOrder voucherOrder){
        Long userId = voucherOrder.getUserId();
        Long voucherId = voucherOrder.getVoucherId();
        long count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
        // 5.2.判断是否存在
        if (count > 0) {
            // 用户已经购买过了
            log.debug("用户已经购买过一次！");
            return;
        }
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucherId).gt("stock", 0)
                .update();

        if (!success) {
            // 扣减失败
            log.debug("库存不足");
            return;
        }
        save(voucherOrder);
    }
}
