package com.example.seckill.service.strategy;

import com.example.seckill.dto.Result;
import com.example.seckill.entity.SeckillVoucher;
import com.example.seckill.entity.VoucherOrder;
import com.example.seckill.service.SeckillVoucherService;
import com.example.seckill.service.VoucherOrderService;
import com.example.seckill.utils.RedisIdWorker;
import com.example.seckill.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
public class VoucherOrderRedissonStrategy {

    @Resource
    private RedisIdWorker redisIdWorker;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private SeckillVoucherService seckillVoucherService;

    @Resource
    private VoucherOrderService voucherOrderService;

    public Result seckillVoucher(Long voucherId) {
        // 1.查询优惠券
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
        // 2.判断秒杀是否开始
        if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
            // 尚未开始
            return Result.fail("秒杀尚未开始！");
        }
        // 3.判断秒杀是否已经结束
        if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
            // 尚未开始
            return Result.fail("秒杀已经结束！");
        }
        // 4.判断库存是否充足
        if (voucher.getStock() < 1) {
            // 库存不足
            return Result.fail("库存不足！");
        }

        return createVoucherOrder(voucherId);
    }

    private Result createVoucherOrder(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        // 创建锁对象
        RLock redisLock = redissonClient.getLock("lock:order:" + userId);
        // 尝试获取锁
        boolean isLock = redisLock.tryLock();
        // 判断
        if (!isLock) {
            // 获取锁失败，直接返回失败或者重试
            log.error("不允许重复下单！");
            return Result.fail("Warning: Repeated Order.");
        }
        try {
            // 5.1.查询订单
            long count = seckillVoucherService.query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            // 5.2.判断是否存在
            if (count > 0) {
                // 用户已经购买过了
                log.error("不允许重复下单！");
                return Result.fail("Warning: Repeated Order.");
            }
            // 6.update database
            //where id = voucher and stock > 0, set stock = stock - 1
            boolean success = seckillVoucherService.update()
                    .setSql("stock = stock - 1")
                    .eq("voucher_id", voucherId).gt("stock", 0)
                    .update();
            if (!success) {
                // 扣减失败
                log.error("库存不足！");
                Result.fail("Warning: Limited Stock.");
            }
            // 7.创建订单
            VoucherOrder voucherOrder = new VoucherOrder();
            long orderId = redisIdWorker.nextId("order");
            voucherOrder.setVoucherId(voucherId);
            voucherOrder.setUserId(userId);
            voucherOrder.setId(orderId);
            voucherOrderService.save(voucherOrder);

            return Result.ok("Success: Create Order #orderId");
        } finally {
            // 释放锁
            redisLock.unlock();
        }
    }
}
