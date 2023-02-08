package com.example.seckill.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.dto.Result;
import com.example.seckill.entity.ShopType;
import com.example.seckill.mapper.ShopTypeMapper;
import com.example.seckill.service.ShopTypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static com.example.seckill.utils.RedisPrefix.CACHE_SHOP_TYPE;

@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements ShopTypeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * <p>Get All Shop Types and Cache. </p>
     * @return {@link Result}
     * @throws JsonProcessingException
     *
     */
    @Override
    public Result getAllTypes() throws JsonProcessingException {
        String key = CACHE_SHOP_TYPE;
        List<String> jsonShopTypeList = stringRedisTemplate.opsForList().range(key, 0, -1);
        ObjectMapper objectMapper = new ObjectMapper();
        if(!jsonShopTypeList.isEmpty()){
            List<ShopType> typeList = new ArrayList<>();
            for (String s : jsonShopTypeList) {
                ShopType shopType = objectMapper.readValue(s, ShopType.class);
                // shopType 是一个对象
                typeList.add(shopType);
            }
            return Result.ok(typeList);
        }
        List<ShopType> typeList = query().orderByAsc("sort").list();
        if(typeList.isEmpty()){
            return Result.fail("Shop type is empty.");
        }
        //store in redis
        List<String> finalJsonShopTypeList = jsonShopTypeList;
        typeList.forEach(shopType -> {
            try {
                finalJsonShopTypeList.add(objectMapper.writeValueAsString(shopType));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        stringRedisTemplate.opsForList().rightPushAll(key, finalJsonShopTypeList);
        return Result.ok(typeList);
    }
}
