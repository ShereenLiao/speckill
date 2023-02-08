package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.dto.Result;
import com.example.seckill.entity.SeckillVoucher;
import com.example.seckill.entity.Voucher;
import com.example.seckill.mapper.VoucherMapper;
import com.example.seckill.service.SeckillVoucherService;
import com.example.seckill.service.VoucherService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.example.seckill.utils.RedisPrefix.SECKILL_STOCK_KEY;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements VoucherService {

    @Resource
    private SeckillVoucherService seckillVoucherService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryVoucherOfShop(Long shopId) {
        List<Voucher> vouchers = this.query().eq("shop_id", String.valueOf(shopId)).list();
        return Result.ok(vouchers);
    }

    @Override
    public Result deleteVoucher(Long voucherId) {
        int count = this.getBaseMapper().deleteById(voucherId);
        if(count == 0){
            Result.fail("Voucher Not Found. ");
        }
        return Result.ok();
    }

    @Override
    @Transactional
    public Result addSeckillVoucher(Voucher voucher) {
        // 保存优惠券
        save(voucher);
        // 保存秒杀信息
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());
        seckillVoucherService.save(seckillVoucher);
        // 保存秒杀库存到Redis中
        stringRedisTemplate.opsForValue().set(SECKILL_STOCK_KEY + voucher.getId(), voucher.getStock().toString());
        return Result.ok();
    }

}
