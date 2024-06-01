package com.demiphea.model.vo.comment;

import com.demiphea.model.vo.user.UserVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Replied
 * 被回复的评论
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Replied {
    /**
     * 被回复的评论ID
     */
    private Long id;

    /**
     * 被回复的评论是否删除
     */
    @JsonProperty("is_deleted")
    private Boolean deleted;

    /**
     * 被回复的评论的用户
     */
    private UserVo user;

    /**
     * 评论的简单文本表示
     */
    private String text;

    /**
     * 被回复的评论的评论时间
     */
    @JsonProperty("create_time")
    private LocalDateTime createTime;
}
