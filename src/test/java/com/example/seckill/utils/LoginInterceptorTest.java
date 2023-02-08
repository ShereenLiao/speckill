package com.example.seckill.utils;

import com.example.seckill.SeckillApplication;
import com.example.seckill.dto.UserDTO;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest(classes = {SeckillApplication.class})
@RunWith(SpringRunner.class)
public class LoginInterceptorTest {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private LoginInterceptor loginInterceptor;

    @Before
    public void setup(){
        loginInterceptor = new LoginInterceptor(stringRedisTemplate);;
    }

    @Test
    public void preHandleTest() throws InvocationTargetException, IllegalAccessException {
        String key = RedisPrefix.LOGIN_USER_KEY + "123456";
        if(loginInterceptor == null){
            System.out.println("error");
            //return;
        }
        if(Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))){
            System.out.println("401..");
            return;
        }
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        if(userMap.isEmpty()){
            System.out.println("401..");
            return;
        }
        Map<String, Object> strUserMap = userMap.entrySet().stream().collect(Collectors.toMap(e -> String.valueOf(e.getKey()), Map.Entry::getValue));
        UserDTO user = new UserDTO();
        BeanUtils.populate(user, strUserMap);
        UserHolder.saveUser(user);
        stringRedisTemplate.expire(key, RedisPrefix.LOGIN_USER_TTL, TimeUnit.MINUTES);
        return;
    }
}
