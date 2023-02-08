package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.dto.Result;
import com.example.seckill.entity.Shop;
import com.fasterxml.jackson.core.JsonProcessingException;


public interface ShopService extends IService<Shop> {

    Result queryById(Long id) throws JsonProcessingException;

    Result update(Shop shop);

    Result queryShopByType(Integer typeId, Integer current, Double x, Double y);
}
