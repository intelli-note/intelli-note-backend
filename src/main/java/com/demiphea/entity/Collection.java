package com.demiphea.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 合集表 实体类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("collection")
public class Collection {

    /**
     * 合集ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称，50字以内
     */
    @TableField("cname")
    private String cname;

    /**
     * 封面图片url，可以为空
     */
    @TableField("cover")
    private String cover;

    /**
     * 简介，255字以内，可以为空
     */
    @TableField("brief_introduction")
    private String briefIntroduction;

    /**
     * 拥有者用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 是否公开
     */
    @TableField("is_open_public")
    private Boolean openPublic;
}
