package com.demiphea.service.inf;

import com.demiphea.entity.Note;
import com.demiphea.entity.User;

/**
 * MessageQueueService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface MessageQueueService {
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
