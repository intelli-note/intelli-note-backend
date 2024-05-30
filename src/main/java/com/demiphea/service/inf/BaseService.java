package com.demiphea.service.inf;

import com.demiphea.entity.*;
import com.demiphea.model.vo.collection.CollectionVo;
import com.demiphea.model.vo.comment.CommentVo;
import com.demiphea.model.vo.favorite.FavoriteVo;
import com.demiphea.model.vo.note.NoteOverviewVo;
import com.demiphea.model.vo.note.NoteVo;
import com.demiphea.model.vo.user.BillVo;
import com.demiphea.model.vo.user.UserVo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * BaseService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface BaseService {
    /**
     * 用户转换
     *
     * @param user 用户实体类
     * @return {@link UserVo} 用户VO
     * @author demiphea
     */
    UserVo convert(@NotNull User user);

    /**
     * 附加状态量
     *
     * @param id 当前用户ID
     * @param userVo 用户VO
     * @return {@link UserVo} 用户VO
     * @author demiphea
     */
    UserVo attachState(@NotNull Long id, @NotNull UserVo userVo);

    /**
     * 笔记转换
     *
     * @param id   当前用户ID
     * @param note 笔记
     * @return {@link NoteOverviewVo} 笔记概览VO
     * @author demiphea
     */
    NoteOverviewVo convert(@Nullable Long id, @NotNull Note note);

    /**
     * 评论转换
     *
     * @param id      当前用户ID
     * @param comment 评论
     * @return {@link CommentVo} 评论VO
     * @author demiphea
     */
    CommentVo convert(@Nullable Long id, @NotNull Comment comment);

    /**
     * 笔记打包
     *
     * @param id   当前用户ID
     * @param note 笔记
     * @return {@link NoteVo} 笔记VO
     * @author demiphea
     */
    NoteVo pack(@Nullable Long id, @NotNull Note note);

    /**
     * 账单转换
     *
     * @param id   当前用户ID
     * @param bill 账单
     * @return {@link BillVo} 账单VO
     * @author demiphea
     */
    BillVo convert(@Nullable Long id, @NotNull Bill bill);

    /**
     * 收藏夹转换
     *
     * @param id       当前用户ID
     * @param favorite 收藏夹
     * @return {@link FavoriteVo} 收藏夹VO
     * @author demiphea
     */
    FavoriteVo convert(@Nullable Long id, @NotNull Favorite favorite);

    /**
     * 合集转换
     *
     * @param id         当前用户ID
     * @param collection 合集
     * @return {@link CollectionVo} 合集VO
     * @author demiphea
     */
    CollectionVo convert(@Nullable Long id, @NotNull Collection collection);
}
