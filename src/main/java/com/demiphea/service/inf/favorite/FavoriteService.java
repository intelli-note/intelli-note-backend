package com.demiphea.service.inf.favorite;

import com.demiphea.model.api.PageResult;
import com.demiphea.model.po.favorite.FavoritePo;
import com.demiphea.model.vo.favorite.FavoriteVo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
}
