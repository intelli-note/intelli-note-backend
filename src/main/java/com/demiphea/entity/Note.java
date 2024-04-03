package com.demiphea.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 笔记表 实体类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("note")
public class Note {

    /**
     * 笔记ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题，50字以内
     */
    @TableField("title")
    private String title;

    /**
     * 封面图片url，可以为空
     */
    @TableField("cover")
    private String cover;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 作者，用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 是否公开
     */
    @TableField("is_open_public")
    private Boolean openPublic;

    /**
     * 价格，免费为null/0
     */
    @TableField("price")
    private BigDecimal price;
}
