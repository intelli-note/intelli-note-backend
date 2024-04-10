package com.demiphea.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户表 实体类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class User {

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名，20字符内
     */
    @TableField("username")
    private String username;

    /**
     * 用户头像url
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 用户简介，255字符内，可为空
     */
    @TableField("biography")
    private String biography;

    /**
     * 性别：null-未知，true-男生，false-女生
     */
    @TableField("gender")
    private Boolean gender;

    /**
     * 用户微信OpenID，不可重复
     */
    @TableField("openid")
    private String openid;

    /**
     * 余额
     */
    @TableField("balance")
    private BigDecimal balance;

    /**
     * 收入
     */
    @TableField("revenue")
    private BigDecimal revenue;
}
