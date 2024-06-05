package com.demiphea.service.inf;

import com.demiphea.entity.Note;
import com.demiphea.entity.User;
import com.demiphea.entity.ViewHistory;
import org.jetbrains.annotations.NotNull;

/**
 * MessageQueueService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface MessageQueueService {
    /**
     * 保存用户浏览历史
     *
     * @param viewHistory 浏览历史
     * @author demiphea
     */
    void saveViewHistory(@NotNull ViewHistory viewHistory);

    /**
     * 克隆收藏夹
     *
     * @param userId   用户ID
     * @param sourceId 源收藏夹ID
     * @param distId   目标收藏夹ID
     * @author demiphea
     */
    void cloneFavorite(@NotNull Long userId, @NotNull Long sourceId, @NotNull Long distId);

    /**
     * 更新评论（点赞数、评论数）
     *
     * @param commentId 评论ID
     * @author demiphea
     */
    void refreshComment(@NotNull Long commentId);

    /**
     * 数据库数据同步至ES
     *
     * @author demiphea
     */
    void syncES();

    /**
     * 向ES中保存/更新用户
     *
     * @param user 用户
     * @author demiphea
     */
    void saveUserToES(User user);

    /**
     * 向ES中保存/更新笔记
     *
     * @param note 笔记
     * @author demiphea
     */
    void saveNoteToES(Note note);

    /**
     * 删除ES中的笔记
     *
     * @param noteId 笔记ID
     * @author demiphea
     */
    void deleteNoteInES(Long noteId);
}
