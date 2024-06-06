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
     * 关注通知
     */
    FOLLOW(0),
    /**
     * 笔记收藏通知
     */
    NOTE_STAR(1),
    /**
     * 合集收藏通知
     */
    COLLECTION_STAR(1),
    /**
     * 评论通知
     */
    COMMENT(2),
    /**
     * 点赞通知
     */
    LIKE(3),
    /**
     * 交易通知
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
