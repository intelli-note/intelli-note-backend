package com.demiphea.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demiphea.entity.Note;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 笔记表 Dao接口
 * @author demiphea
 * @since 17.0.9
 */
public interface NoteDao extends BaseMapper<Note> {
    /**
     * 查看用户购买的笔记
     *
     * @param userId 用户ID
     * @return {@link List<Note>} 笔记列表
     * @author demiphea
     */
    List<Note> listUserBuy(@NotNull Long userId);

    /**
     * 查看用户笔记浏览记录
     *
     * @param userId 用户ID
     * @return {@link List<Note>} 笔记列表
     * @author demiphea
     */
    List<Note> listUserView(@NotNull Long userId);

    /**
     * 查看合集内的笔记
     *
     * @param userId       用户ID
     * @param collectionId 合集ID
     * @return {@link List<Note>} 笔记列表
     * @author demiphea
     */
    List<Note> listCollectionNotes(@Nullable Long userId, @NotNull Long collectionId);
}
