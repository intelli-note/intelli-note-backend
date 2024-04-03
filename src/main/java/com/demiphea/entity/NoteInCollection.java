package com.demiphea.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 笔记-in-合集表 实体类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("note_in_collection")
public class NoteInCollection {

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
