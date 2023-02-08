package com.example.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckill.dto.Result;
import com.example.seckill.entity.ShopType;
import com.example.seckill.service.ShopTypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {
    @Resource
    private ShopTypeService shopTypeService;

    @GetMapping("/list")
    public Result getAllShopTypes() throws JsonProcessingException {
        return shopTypeService.getAllTypes();
    }
}
