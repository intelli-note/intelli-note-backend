package com.demiphea.service.inf;

import com.demiphea.entity.es.NoteDoc;
import com.demiphea.entity.es.UserDoc;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * ElasticSearchService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface ElasticSearchService {
    /**
     * 查找用户
     *
     * @param key      关键词
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return {@link List<UserDoc>} 用户索引
     * @author demiphea
     */
    List<UserDoc> searchUser(@NotNull String key, @NotNull Integer pageNum, @NotNull Integer pageSize);

    /**
     * 查找笔记
     *
     * @param key      关键字
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return {@link List<NoteDoc>} 笔记索引
     * @author demiphea
     */
    List<NoteDoc> searchNote(@NotNull String key, @NotNull Integer pageNum, @NotNull Integer pageSize);
}
