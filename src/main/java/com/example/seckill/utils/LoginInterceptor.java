package com.example.seckill.utils;

import com.example.seckill.dto.UserDTO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class LoginInterceptor implements HandlerInterceptor {
    private StringRedisTemplate stringRedisTemplate;
    public LoginInterceptor(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("authorization");
        if(StringUtils.isBlank(token)){
            response.setStatus(401);
            return false;
        }
        String key = RedisPrefix.LOGIN_USER_KEY + token;
        if(!stringRedisTemplate.hasKey(key)){
            response.setStatus(401);
            return false;
        }
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        if(userMap.isEmpty()){
            response.setStatus(401);
            return false;
        }
        Map<String, Object> strUserMap = userMap.entrySet().stream().collect(Collectors.toMap(e -> String.valueOf(e.getKey()), Map.Entry::getValue));
        UserDTO user = new UserDTO();
        BeanUtils.populate(user, strUserMap);
        UserHolder.saveUser(user);
        stringRedisTemplate.expire(key, RedisPrefix.LOGIN_USER_TTL, TimeUnit.MINUTES);
        return true;
    }
}
