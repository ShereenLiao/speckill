package com.example.seckill.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeckillMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long voucherId;
    private Long orderId;
}
