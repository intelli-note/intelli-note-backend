package com.demiphea.service.inf.favorite;

import com.demiphea.model.api.PageResult;
import com.demiphea.model.po.favorite.FavoritePo;
import com.demiphea.model.vo.favorite.FavoriteVo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * FavoriteService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface FavoriteService {
    /**
     * 新建收藏夹
     *
     * @param id         当前用户ID
     * @param favoritePo 收藏夹PO
     * @return {@link FavoriteVo} 收藏夹VO
     * @author demiphea
     */
    FavoriteVo insertFavorite(@NotNull Long id, @NotNull FavoritePo favoritePo);

    /**
     * 修改收藏夹
     *
     * @param id         当前用户ID
     * @param favoritePo 收藏夹PO
     * @return {@link FavoriteVo} 收藏夹VO
     * @author demiphea
     */
    FavoriteVo updateFavorite(@NotNull Long id, @NotNull FavoritePo favoritePo);

    /**
     * 删除收藏夹
     *
     * @param id         当前用户ID
     * @param favoriteId 收藏夹ID
     * @author demiphea
     */
    void deleteFavorite(@NotNull Long id, @NotNull Long favoriteId);


    /**
     * 分页获取用户收藏夹
     *
     * @param id       当前用户ID
     * @param userId   查询用户ID
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return {@link PageResult} 分页结果
     * @author demiphea
     */
    PageResult listFavorites(@Nullable Long id, @Nullable Long userId, @NotNull Integer pageNum, @NotNull Integer pageSize);

    /**
     * 向收藏夹内添加笔记或合集
     *
     * @param id            当前用户ID
     * @param noteIds       笔记ID列表
     * @param collectionIds 合集ID列表
     * @param favoriteIds   要添加的收藏夹ID列表
     * @return 成功数
     * @author demiphea
     */
    int insertNoteOrCollectionToFavorite(@NotNull Long id, @Nullable List<Long> noteIds, @Nullable List<Long> collectionIds, @NotNull List<Long> favoriteIds);

    /**
     * 收藏夹内删除笔记或合集
     *
     * @param id            当前用户ID
     * @param favoriteId    收藏夹ID
     * @param noteIds       笔记ID列表
     * @param collectionIds 合集ID列表
     * @return 成功数
     * @author demiphea
     */
    int deleteNoteOrCollectionInFavorite(@NotNull Long id, @NotNull Long favoriteId, @Nullable List<Long> noteIds, @Nullable List<Long> collectionIds);

    // TODO 分页获取用户收藏夹内的内容
}
