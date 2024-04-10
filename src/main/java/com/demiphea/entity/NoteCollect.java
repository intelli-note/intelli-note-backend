package com.demiphea.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 合集收录表 实体类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("note_collect")
public class NoteCollect {

    /**
     * 笔记ID
     */
    @TableField("note_id")
    private Long noteId;

    /**
     * 合集ID
     */
    @TableField("collection_id")
    private Long collectionId;
}
