package com.demiphea.service.inf.comment;

import com.demiphea.model.dto.comment.CommentDto;
import com.demiphea.model.vo.comment.CommentVo;
import org.jetbrains.annotations.NotNull;

/**
 * CommentService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface CommentService {
    /**
     * 插入评论
     *
     * @param id         当前用户ID
     * @param commentDto 评论DTO
     * @return {@link CommentVo} 评论VO
     * @author demiphea
     */
    CommentVo insertComment(@NotNull Long id, @NotNull CommentDto commentDto);

    /**
     * 删除评论
     *
     * @param id        当前用户ID
     * @param commentId 评论ID
     * @author demiphea
     */
    void deleteComment(@NotNull Long id, @NotNull Long commentId);
}
