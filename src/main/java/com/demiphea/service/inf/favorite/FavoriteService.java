package com.demiphea.service.inf.favorite;

import com.demiphea.model.po.favorite.FavoritePo;
import com.demiphea.model.vo.favorite.FavoriteVo;
import org.jetbrains.annotations.NotNull;

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
}
