package com.demiphea.model.vo.notice;

import com.demiphea.model.vo.comment.CommentVo;
import com.demiphea.model.vo.note.NoteOverviewVo;
import com.demiphea.model.vo.user.UserVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CommentNotice
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentNotice implements INotice {
    /**
     * 评论通知类型
     */
    private Type type;
    /**
     * 评论所在笔记
     */
    private NoteOverviewVo note;
    /**
     * 目标评论：回复/被点赞的评论
     */
    private CommentVo target;
    /**
     * 评论所在上下文
     */
    private List<CommentVo> context;

    // ------以下属性点赞类型特有------
    /**
     * 点赞者
     */
    private UserVo liker;
    /**
     * 点赞时间
     */
    @JsonProperty("like_time")
    private LocalDateTime likeTime;

    /**
     * 评论通知类型
     */
    public enum Type {
        /**
         * 回复
         */
        REPLY(0),
        /**
         * 点赞
         */
        LIKE(1),
        ;
        @JsonProperty
        public final int type;

        Type(int type) {
            this.type = type;
        }
    }
}
