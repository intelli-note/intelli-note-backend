package com.demiphea.service.inf.comment;

import com.demiphea.model.api.PageResult;
import com.demiphea.model.dto.comment.CommentDto;
import com.demiphea.model.vo.comment.CommentVo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    /**
     * 分页获取笔记一级评论
     *
     * @param id       当前用户ID
     * @param noteId   笔记ID
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return {@link PageResult} 结果
     * @author demiphea
     */
    PageResult listFirstComments(@Nullable Long id, @NotNull Long noteId, @NotNull Integer pageNum, @NotNull Integer pageSize);

    /**
     * 分页获取笔记二级评论
     *
     * @param id       当前用户ID
     * @param rootId   根评论（一级评论）ID
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return {@link PageResult} 结果
     * @author demiphea
     */
    PageResult listSecondComments(@Nullable Long id, @NotNull Long rootId, @NotNull Integer pageNum, @NotNull Integer pageSize);
}
