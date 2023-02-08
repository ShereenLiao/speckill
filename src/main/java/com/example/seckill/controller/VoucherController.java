package com.example.seckill.controller;



import com.example.seckill.dto.Result;
import com.example.seckill.entity.Voucher;
import com.example.seckill.service.VoucherService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 虎哥
 */
@RestController
@RequestMapping("/voucher")
public class VoucherController {
    @Resource
    private VoucherService voucherService;

    @GetMapping("/list/{shopId}")
    public Result getVoucherByShop(@PathVariable("shopId") Long shopId) {
        return voucherService.queryVoucherOfShop(shopId);
    }

    @PostMapping("/list/{shopId}")
    public Result addVoucher(@PathVariable("shopId") Long shopId, @RequestBody Voucher voucher) {
        voucher.setShopId(shopId);
        voucherService.save(voucher);
        return Result.ok(voucher.getId());
    }

    @DeleteMapping("/{voucherId}")
    public Result deleteVoucherById(@PathVariable("voucherId") Long voucherId) {
        return voucherService.deleteVoucher(voucherId);
    }

    @PostMapping("/{voucherId}")
    public Result updateVoucher(@PathVariable("voucherId") Long voucherId, @RequestBody Voucher voucher) {
        voucherService.save(voucher);
        return Result.ok(voucher);
    }

    @GetMapping("/{voucherId}")
    public Result getVouchersById(@PathVariable("voucherId") Long voucherId) {
        Voucher voucher = voucherService.query().eq("id", String.valueOf(voucherId)).getEntity();
        if(voucher == null){
            return Result.fail("Voucher not found. ");
        }
        else{
            return Result.ok(voucher);
        }
    }

    /**
     * 新增秒杀券
     * @param voucher 优惠券信息，包含秒杀信息
     * @return 优惠券id
     */
    @PostMapping("/seckill")
    public Result addSeckillVoucher(@RequestBody Voucher voucher) {
        voucherService.addSeckillVoucher(voucher);
        return Result.ok(voucher.getId());
    }

}
