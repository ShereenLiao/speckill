package com.example.seckill.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.seckill.dto.Result;
import com.example.seckill.entity.Shop;
import com.example.seckill.service.ShopService;
import com.example.seckill.utils.SystemConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Resource
    public ShopService shopService;

    /**
     * Query Shop By ID
     * @param id Shop ID
     * @return Shop Information
     */
    @GetMapping("/{id}")
    public Result queryShopById(@PathVariable("id") Long id) throws JsonProcessingException {
        return shopService.queryById(id);
    }

    /**
     * Update Shop Information.
     * @param shop Shop Information
     * @return null
     */
    @PutMapping("/{id}")
    public Result updateShop(@PathVariable("id") Long id, @RequestBody Shop shop) {
        return shopService.update(shop);
    }



    /**
     * 根据商铺类型分页查询商铺信息
     * @param typeId 商铺类型
     * @param current 页码
     * @return 商铺列表
     */
    @GetMapping("/of/type")
    public Result queryShopByType(
            @RequestParam("typeId") Integer typeId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "x", required = false) Double x,
            @RequestParam(value = "y", required = false) Double y
    ) {
        return shopService.queryShopByType(typeId, current, x, y);
    }

    /**
     * 根据商铺名称关键字分页查询商铺信息
     * @param name 商铺名称关键字
     * @param current 页码
     * @return 商铺列表
     */
    @GetMapping("/of/name")
    public Result queryShopByName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") Integer current
    ) {
        // 根据类型分页查询
        Page<Shop> page = shopService.query()
                .like(StringUtils.isNotBlank(name), "name", name)
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 返回数据
        return Result.ok(page.getRecords());
    }
}