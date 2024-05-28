package com.demiphea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.dao.CollectionFavoriteDao;
import com.demiphea.dao.FavoriteDao;
import com.demiphea.dao.NoteFavoriteDao;
import com.demiphea.entity.CollectionFavorite;
import com.demiphea.entity.Favorite;
import com.demiphea.entity.NoteFavorite;
import com.demiphea.exception.common.ObjectDoesNotExistException;
import com.demiphea.exception.common.PermissionDeniedException;
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

    /**
     * 取消用户默认收藏夹
     *
     * @param userId 用户ID
     * @author demiphea
     */
    private void cancelDefaultFavorite(Long userId) {
        Favorite defaultFavorite = favoriteDao.selectOne(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getOptionDefault, true)
        );
        if (defaultFavorite != null) {
            defaultFavorite.setOptionDefault(false);
            favoriteDao.updateById(defaultFavorite);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FavoriteVo insertFavorite(@NotNull Long id, @NotNull FavoritePo favoritePo) {
        if (favoritePo.getConfiguration().isDefaultConfig()) {
            cancelDefaultFavorite(id);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FavoriteVo updateFavorite(@NotNull Long id, @NotNull FavoritePo favoritePo) {
        if (favoritePo.getConfiguration().isDefaultConfig()) {
            cancelDefaultFavorite(id);
        }
        Favorite favorite = favoriteDao.selectById(favoritePo.getFavoriteId());
        if (favorite == null) {
            throw new ObjectDoesNotExistException("收藏夹不存在或已删除");
        }
        if (!permissionService.checkFavoriteAdminPermission(id, favorite)) {
            throw new PermissionDeniedException("权限拒绝访问操作");
        }
        favorite.setFname(favoritePo.getName());
        favorite.setBriefIntroduction(favoritePo.getDescription());
        favorite.setOptionPublic(favoritePo.getConfiguration().isPublicConfig());
        favorite.setOptionDefault(favoritePo.getConfiguration().isDefaultConfig());
        favoriteDao.updateById(favorite);
        return baseService.convert(id, favoriteDao.selectById(favorite.getId()));
    }

    @Override
    public void deleteFavorite(@NotNull Long id, @NotNull Long favoriteId) {
        if (!permissionService.checkFavoriteAdminPermission(id, favoriteId)) {
            throw new PermissionDeniedException("权限拒绝访问操作");
        }
        favoriteDao.deleteById(favoriteId);
    }
}
