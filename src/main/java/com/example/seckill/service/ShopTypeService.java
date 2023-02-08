package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.dto.Result;
import com.example.seckill.entity.ShopType;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ShopTypeService extends IService<ShopType> {
    Result getAllTypes() throws JsonProcessingException;
}
