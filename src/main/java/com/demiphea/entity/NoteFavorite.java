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
 * 笔记收藏表 实体类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("note_favorite")
public class NoteFavorite {

    /**
     * 收藏联系ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 笔记ID
     */
    @TableField("note_id")
    private Long noteId;

    /**
     * 收藏夹ID
     */
    @TableField("favorite_id")
    private Long favoriteId;

    /**
     * 收藏时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
