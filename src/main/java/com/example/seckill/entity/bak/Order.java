package com.example.seckill.entity.bak;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.seckill.enums.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * Order Table
 * </p>
 *
 * @author Xiaoxuan Liao
 * @since 2023-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long productId;
    private Long deliveryAddrId;
    private String productName;
    private Integer productQuantity;
    private BigDecimal productPrice;

    /**
     * Order Status. 0: Pending，1: Unshipped，2: InTransit，3: Delivered，4: Returned，5: Completed
     */
    private int status;
    private LocalDateTime createDate;
    private LocalDateTime payDate;
}
