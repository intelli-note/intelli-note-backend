package com.demiphea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.dao.CollectionFavoriteDao;
import com.demiphea.dao.FavoriteDao;
import com.demiphea.dao.NoteFavoriteDao;
import com.demiphea.entity.CollectionFavorite;
import com.demiphea.entity.Favorite;
import com.demiphea.entity.NoteFavorite;
import com.demiphea.model.po.favorite.FavoritePo;
import com.demiphea.model.vo.favorite.FavoriteVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.PermissionService;
import com.demiphea.service.inf.favorite.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * FavoriteServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final BaseService baseService;
    private final PermissionService permissionService;
    private final FavoriteDao favoriteDao;
    private final NoteFavoriteDao noteFavoriteDao;
    private final CollectionFavoriteDao collectionFavoriteDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FavoriteVo insertFavorite(@NotNull Long id, @NotNull FavoritePo favoritePo) {
        if (favoritePo.getConfiguration().isDefaultConfig()) {
            Favorite defaultFavorite = favoriteDao.selectOne(new LambdaQueryWrapper<Favorite>()
                    .eq(Favorite::getUserId, id)
                    .eq(Favorite::getOptionDefault, true)
            );
            if (defaultFavorite != null) {
                defaultFavorite.setOptionDefault(false);
                favoriteDao.updateById(defaultFavorite);
            }
        }
        Favorite favorite = new Favorite(
                null,
                favoritePo.getName(),
                favoritePo.getDescription(),
                id,
                LocalDateTime.now(),
                favoritePo.getConfiguration().isPublicConfig(),
                favoritePo.getConfiguration().isDefaultConfig()
        );
        favoriteDao.insert(favorite);
        if (favoritePo.getFavoriteId() != null) {
            // 异步克隆收藏夹
            CompletableFuture.runAsync(() -> {
                Long favoriteId = favoritePo.getFavoriteId();
                if (!permissionService.checkFavoriteViewPermission(id, favoriteId)) {
                    return;
                }
                List<Long> noteIds = noteFavoriteDao.selectList(new LambdaQueryWrapper<NoteFavorite>()
                        .eq(NoteFavorite::getFavoriteId, favoriteId)
                ).stream().map(NoteFavorite::getNoteId).toList();
                for (Long noteId : noteIds) {
                    try {
                        noteFavoriteDao.insert(new NoteFavorite(null, noteId, favorite.getId(), LocalDateTime.now()));
                    } catch (Exception e) {
                        // ignore
                    }
                }
                List<Long> collectionIds = collectionFavoriteDao.selectList(new LambdaQueryWrapper<CollectionFavorite>()
                        .eq(CollectionFavorite::getFavoriteId, favoriteId)
                ).stream().map(CollectionFavorite::getCollectionId).toList();
                for (Long collectionId : collectionIds) {
                    try {
                        collectionFavoriteDao.insert(new CollectionFavorite(null, collectionId, favorite.getId(), LocalDateTime.now()));
                    } catch (Exception e) {
                        // ignore
                    }
                }
            });
        }
        return baseService.convert(id, favoriteDao.selectById(favorite.getId()));
    }
}
