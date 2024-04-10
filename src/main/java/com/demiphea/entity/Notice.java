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
 * 通知表 实体类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("notice")
public class Notice {

    /**
     * 通知ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 通知类型：0-关注通知，1-收藏通知，2-评论通知，3-点赞通知，4-交易通知
     */
    @TableField("type")
    private Long type;

    /**
     * 关联关注表ID，可选
     */
    @TableField("link_follow_id")
    private Long linkFollowId;

    /**
     * 关联笔记收藏表ID，可选
     */
    @TableField("link_star_note_id")
    private Long linkStarNoteId;

    /**
     * 关联合集收藏表ID，可选
     */
    @TableField("link_star_collection_id")
    private Long linkStarCollectionId;

    /**
     * 关联评论表ID，可选
     */
    @TableField("link_comment_id")
    private Long linkCommentId;

    /**
     * 关联评论点赞表ID，可选
     */
    @TableField("link_comment_like_id")
    private Long linkCommentLikeId;

    /**
     * 关联账单ID，可选
     */
    @TableField("link_bill_id")
    private Long linkBillId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 通知时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 是否已读
     */
    @TableField("is_read")
    private Boolean read;
}
