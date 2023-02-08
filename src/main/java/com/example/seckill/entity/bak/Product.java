package com.example.seckill.entity.bak;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * Product Table
 * </p>
 *
 * @author Xiaoxuan Liao
 * @since 2023-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String productName;
    private String productTitle;
    private String productImg;
    private String productDetail;
    private BigDecimal productPrice;

    /**
     * Product Stock. -1: unlimited
     */
    private Integer productStock;


}
