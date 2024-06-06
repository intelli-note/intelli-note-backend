package com.demiphea.model.bo.notice;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * NoticeType
 *
 * @author demiphea
 * @since 17.0.9
 */
public enum NoticeType {
    /**
     * 关注通知（用户关注）
     */
    FOLLOW(0),
    /**
     * 笔记收藏通知（笔记收藏）
     */
    NOTE_STAR(1),
    /**
     * 合集收藏通知（合集收藏）
     */
    COLLECTION_STAR(1),
    /**
     * 评论通知（评论笔记、回复评论）
     */
    COMMENT(2),
    /**
     * 点赞通知（点赞评论）
     */
    LIKE(3),
    /**
     * 交易通知（购买笔记、提现/充值）
     */
    TRADE(4),

    /**
     * 所有通知
     */
    ALL(-1);

    @JsonProperty
    public final int type;

    NoticeType(int type) {
        this.type = type;
    }
}
