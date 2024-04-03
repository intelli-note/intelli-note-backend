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
 * 评论表 实体类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_comment")
public class Comment {

    /**
     * 评论ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 评论文本，可选
     */
    @TableField("content")
    private String content;

    /**
     * 评论图片列表，可选
     */
    @TableField("image_list")
    private String imageList;

    /**
     * 音频url，可选
     */
    @TableField("audio")
    private String audio;

    /**
     * 视频url，可选
     */
    @TableField("video")
    private String video;

    /**
     * 关联笔记ID，可选
     */
    @TableField("link_note_id")
    private Long linkNoteId;

    /**
     * 所属笔记ID
     */
    @TableField("note_id")
    private Long noteId;

    /**
     * 根评论ID
     */
    @TableField("root_id")
    private Long rootId;

    /**
     * 父评论ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 评论时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
