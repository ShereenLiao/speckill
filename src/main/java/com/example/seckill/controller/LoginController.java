package com.example.seckill.controller;


import com.example.seckill.dto.LoginFormDTO;
import com.example.seckill.dto.Result;
import com.example.seckill.dto.UserDTO;
import com.example.seckill.service.UserService;
import com.example.seckill.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String toLogin() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result doLogin(@RequestBody LoginFormDTO loginForm , HttpSession session) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return userService.login(loginForm, session);
    }

    @GetMapping("/me")
    public Result me(){
        UserDTO user = UserHolder.getUser();
        return Result.ok(user);
    }

}
