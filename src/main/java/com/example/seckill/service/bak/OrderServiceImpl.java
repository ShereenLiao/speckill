package com.example.seckill.service.bak;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.entity.bak.Order;
import com.example.seckill.mapper.bak.OrderMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Order Table 服务实现类
 * </p>
 *
 * @author Xiaoxuan Liao
 * @since 2023-02-05
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
