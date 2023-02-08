package com.example.seckill.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Xiaoxuan Liao
 * @since 2023-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String phone;
    private String nickname;
    private String password;
    private String icon = "";
//    private String salt;
    //private String head; //????
    @TableField(fill = FieldFill.INSERT) //创建时自动填充
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)//创建与修改时自动填充
    private LocalDateTime updateTime;

}
