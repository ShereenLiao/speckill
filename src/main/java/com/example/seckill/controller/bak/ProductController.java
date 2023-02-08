package com.example.seckill.controller.bak;


import com.example.seckill.dto.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * Product Table 前端控制器
 * </p>
 *
 * @author Xiaoxuan Liao
 * @since 2023-02-05
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/list")
    public Result getProducts(){
        return new Result();
    }
}
