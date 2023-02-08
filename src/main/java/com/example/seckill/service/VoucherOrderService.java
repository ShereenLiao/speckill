package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.dto.Result;
import com.example.seckill.entity.VoucherOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 *  服务类
 * </p>
 */
public interface VoucherOrderService extends IService<VoucherOrder> {
    Result seckillVoucher(Long voucherId) throws JsonProcessingException;

    @Transactional
    void updateVoucherDatabase(VoucherOrder voucherOrder);
}
