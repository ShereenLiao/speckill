package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.dto.LoginFormDTO;
import com.example.seckill.dto.Result;
import com.example.seckill.entity.User;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Xiaoxuan Liao
 * @since 2023-02-05
 */
public interface UserService extends IService<User> {
    Result sendCode(String phone, HttpSession session);
    Result login(LoginFormDTO loginForm, HttpSession session);

    Result logout(HttpSession session);

    Result signIn();
}
