package com.demiphea.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 笔记查看历史表 实体类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("view_history")
public class ViewHistory {

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 笔记ID
     */
    @TableField("note_id")
    private Long noteId;

    /**
     * 查看时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
