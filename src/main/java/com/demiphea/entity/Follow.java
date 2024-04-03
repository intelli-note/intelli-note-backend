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
 * 关注表 实体类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("follow")
public class Follow {

    /**
     * 关注联系ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 当前用户ID
     */
    @TableField("follower_id")
    private Long followerId;

    /**
     * 被关注用户ID
     */
    @TableField("follow_id")
    private Long followId;

    /**
     * 关注时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
