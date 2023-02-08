package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.dto.Result;
import com.example.seckill.entity.Voucher;



public interface VoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    Result addSeckillVoucher(Voucher voucher);

    Result deleteVoucher(Long voucherId);
}
