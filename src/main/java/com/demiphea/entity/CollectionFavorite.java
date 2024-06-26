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
 * 合集收藏表 实体类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("collection_favorite")
public class CollectionFavorite {

    /**
     * 合集收藏联系ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 合集ID
     */
    @TableField("collection_id")
    private Long collectionId;

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
