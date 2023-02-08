package com.example.seckill.config;

import com.example.seckill.dto.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
        Map<String, Object> result = new HashMap<>(3);
        String message =exception.getMessage()+request.getRequestURL().toString();
        return Result.fail(message);
    }
}


