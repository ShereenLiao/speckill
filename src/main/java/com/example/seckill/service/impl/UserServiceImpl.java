package com.example.seckill.service.impl;

import com.example.seckill.dto.LoginFormDTO;
import com.example.seckill.dto.Result;
import com.example.seckill.dto.UserDTO;
import com.example.seckill.entity.User;
import com.example.seckill.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.service.UserService;
import com.example.seckill.utils.RegexUtils;
import com.example.seckill.utils.UserHolder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.seckill.utils.RedisPrefix.*;
import static com.example.seckill.utils.SystemConstants.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Xiaoxuan Liao
 * @since 2023-02-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 1. check if the phone number is valid
     *  if not, return  fail message
     *  if yes, continue
     * 2. store verification code in redis, with expiration time: 2 minutes
     * 3. return success message
     * **/
    @Override
    public Result sendCode(String phone, HttpSession session) {
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("Phone Number is invalud. ");
        }
        String code = String.valueOf(RandomUtils.nextInt(100000, 999999));
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        log.debug("Verification Code for "+phone +" : " + code);
        return Result.ok();
    }

    /**
     * User can log in using phone + password / phone + code
     * 1. check if the phone number is valid
     *      if yes, continue
     *      if not, return fail
     * 2. check if user's password is valid
     *      if yes, continue
     *      if not, return fail
     * 2. check if the verification code is valid
     *      if yes, continue
     *      if not, return fail
     * 3. check if the user exists in sql database
     *      if yes, continue
     *      if not, store in db
     * 4. store user in redis key <tokenKey, map>
     * */
    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session){
        //1. 校验手机号和验证码
        String phone = loginForm.getPhone();
        if(RegexUtils.isPhoneInvalid(phone)){
            //2。如果不符合，返回错误信息
            return Result.fail("phone number is not valid");
        }
        String code = loginForm.getCode();
        String password = loginForm.getPassword();
        User user = query().eq("phone", phone).one();
        if(StringUtils.isBlank(code)){
            //password + phone
            if(!StringUtils.equals(password, user.getPassword())){
                return Result.fail("The Password is not valid");
            }
        }
        else{
            //verify using code
            Object cachedCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY+phone);
            if(cachedCode == null || !cachedCode.toString().equals(code)){
                return Result.fail("code is not valid");
            }
        }
        //2.2 判断用户是否存在
        if(user == null){
            user = createUserWithPhone(phone);
        }
        String token = UUID.randomUUID().toString();
        //copy properties from user to userDTO
        UserDTO userDTO = new UserDTO();
        org.springframework.beans.BeanUtils.copyProperties(user, userDTO);
        //convert userDTO to map(redis storage type)
        try{
            Map<String, String> userMap = org.apache.commons.beanutils.BeanUtils.describe(userDTO);
            stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY+ token, userMap);
            stringRedisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TTL, TimeUnit.MINUTES);
            return Result.ok(token);
        }
        catch(RuntimeException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            return Result.fail("Information not valid");
        }
    }

    @Override
    public Result logout(HttpSession session) {
        String token = (String)session.getAttribute("token");
        if(!StringUtils.isBlank(token)){
            stringRedisTemplate.delete(token);
        }
        UserHolder.removeUser();
        return Result.ok("Logout Success.");
    }

    @Override
    public Result signIn() {
        // 1.获取当前登录用户
        Long userId = UserHolder.getUser().getId();
        // 2.获取日期
        LocalDateTime now = LocalDateTime.now();
        // 3.拼接key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = USER_SIGN_KEY + userId + keySuffix;
        // 4.获取今天是本月的第几天
        int dayOfMonth = now.getDayOfMonth();
        // 5.写入Redis SETBIT key offset 1
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
        return Result.ok();
    }

    private User createUserWithPhone(String phone){
        User user = new User();
        user.setPhone(phone);
        user.setNickname(USER_NICK_NAME_PREFIX + RandomStringUtils.randomAlphabetic(USER_NICK_NAME_SALT_LENGTH));
        this.save(user);
        return user;
    }
}
