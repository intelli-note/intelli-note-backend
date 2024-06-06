package com.demiphea.service.impl.favorite;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.dao.*;
import com.demiphea.entity.*;
import com.demiphea.exception.common.CommonServiceException;
import com.demiphea.exception.common.ObjectDoesNotExistException;
import com.demiphea.exception.common.PermissionDeniedException;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.bo.notice.NoticeType;
import com.demiphea.model.dto.favorite.FavoriteDto;
import com.demiphea.model.po.FavoriteObject;
import com.demiphea.model.vo.favorite.FavoriteVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.MessageQueueService;
import com.demiphea.service.inf.PermissionService;
import com.demiphea.service.inf.SystemService;
import com.demiphea.service.inf.favorite.FavoriteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

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
    private final NoteDao noteDao;
    private final CollectionDao collectionDao;
    private final NoteFavoriteDao noteFavoriteDao;
    private final CollectionFavoriteDao collectionFavoriteDao;
    private final MessageQueueService messageQueueService;
    private final SystemService systemService;

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
    public FavoriteVo insertFavorite(@NotNull Long id, @NotNull FavoriteDto favoriteDto) {
        if (favoriteDto.getConfiguration().isDefaultConfig()) {
            cancelDefaultFavorite(id);
        }
        Favorite favorite = new Favorite(
                null,
                favoriteDto.getName(),
                favoriteDto.getDescription(),
                id,
                LocalDateTime.now(),
                favoriteDto.getConfiguration().isPublicConfig(),
                favoriteDto.getConfiguration().isDefaultConfig()
        );
        favoriteDao.insert(favorite);
        if (favoriteDto.getFavoriteId() != null) {
            // 异步克隆收藏夹
            messageQueueService.cloneFavorite(id, favoriteDto.getFavoriteId(), favorite.getId());
        }
        return baseService.convert(id, favoriteDao.selectById(favorite.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FavoriteVo updateFavorite(@NotNull Long id, @NotNull FavoriteDto favoriteDto) {
        if (favoriteDto.getConfiguration().isDefaultConfig()) {
            cancelDefaultFavorite(id);
        }
        Favorite favorite = favoriteDao.selectById(favoriteDto.getFavoriteId());
        if (favorite == null) {
            throw new ObjectDoesNotExistException("收藏夹不存在或已删除");
        }
        if (!permissionService.checkFavoriteAdminPermission(id, favorite)) {
            throw new PermissionDeniedException("权限拒绝访问操作");
        }
        favorite.setFname(favoriteDto.getName());
        favorite.setBriefIntroduction(favoriteDto.getDescription());
        favorite.setOptionPublic(favoriteDto.getConfiguration().isPublicConfig());
        favorite.setOptionDefault(favoriteDto.getConfiguration().isDefaultConfig());
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

    @Override
    public PageResult listFavorites(@Nullable Long id, @Nullable Long userId, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        if (id == null && userId == null) {
            throw new CommonServiceException("如需查询自己的收藏夹，请登录");
        }
        if (userId == null) {
            userId = id;
        }
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Favorite> favorites = favoriteDao.selectList(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .and(w0 -> w0.eq(Favorite::getOptionPublic, true).or(id != null, w1 -> w1.eq(Favorite::getUserId, id)))
                .orderByDesc(Favorite::getCreateTime));
        PageInfo<Favorite> pageInfo = new PageInfo<>(favorites);
        List<FavoriteVo> list = favorites.stream().map(favorite -> baseService.convert(id, favorite)).toList();
        PageResult result = new PageResult(pageInfo, list);
        page.close();
        return result;
    }

    @Override
    public int insertNoteOrCollectionToFavorite(@NotNull Long id, @Nullable List<Long> noteIds, @Nullable List<Long> collectionIds, @NotNull List<Long> favoriteIds) {
        int count = 0;
        if (noteIds != null && !noteIds.isEmpty()) {
            for (int i = 0; i < noteIds.size(); i++) {
                Long noteId = noteIds.get(i);
                try {
                    Assert.isTrue(permissionService.checkNoteReadPermission(id, noteId), "[" + noteId + "]: without read note permission!");
                } catch (Exception e) {
                    continue;
                }
                for (int j = 0; j < favoriteIds.size(); j++) {
                    Long favoriteId = favoriteIds.get(j);
                    try {
                        Assert.isTrue(permissionService.checkFavoriteAdminPermission(id, favoriteId), "[" + favoriteId + "]: without favorite admin permission!");
                    } catch (Exception e) {
                        continue;
                    }
                    try {
                        NoteFavorite entity = new NoteFavorite(null, noteId, favoriteId, LocalDateTime.now());
                        count += noteFavoriteDao.insert(entity);
                        systemService.publishNotice(id, NoticeType.NOTE_STAR, entity.getId());
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        }
        if (collectionIds != null && !collectionIds.isEmpty()) {
            for (int i = 0; i < collectionIds.size(); i++) {
                Long collectionId = collectionIds.get(i);
                try {
                    Assert.isTrue(permissionService.checkCollectionViewPermission(id, collectionId), "[" + collectionId + "]: without view collection permission!");
                } catch (Exception e) {
                    continue;
                }
                for (int j = 0; j < favoriteIds.size(); j++) {
                    Long favoriteId = favoriteIds.get(j);
                    try {
                        Assert.isTrue(permissionService.checkFavoriteAdminPermission(id, favoriteId), "[" + favoriteId + "]: without favorite admin permission!");
                    } catch (Exception e) {
                        continue;
                    }
                    try {
                        CollectionFavorite entity = new CollectionFavorite(null, collectionId, favoriteId, LocalDateTime.now());
                        count += collectionFavoriteDao.insert(entity);
                        systemService.publishNotice(id, NoticeType.COLLECTION_STAR, entity.getId());
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        }
        return count;
    }

    @Override
    public int deleteNoteOrCollectionInFavorite(@NotNull Long id, @NotNull Long favoriteId, @Nullable List<Long> noteIds, @Nullable List<Long> collectionIds) {
        if (!permissionService.checkFavoriteAdminPermission(id, favoriteId)) {
            throw new PermissionDeniedException("权限拒绝访问操作");
        }
        int count = 0;
        if (noteIds != null && !noteIds.isEmpty()) {
            for (int i = 0; i < noteIds.size(); i++) {
                count += noteFavoriteDao.delete(new LambdaQueryWrapper<NoteFavorite>()
                        .eq(NoteFavorite::getFavoriteId, favoriteId)
                        .eq(NoteFavorite::getNoteId, noteIds.get(i)));
            }
        }

        if (collectionIds != null && !collectionIds.isEmpty()) {
            for (int i = 0; i < collectionIds.size(); i++) {
                count += collectionFavoriteDao.delete(new LambdaQueryWrapper<CollectionFavorite>()
                        .eq(CollectionFavorite::getFavoriteId, favoriteId)
                        .eq(CollectionFavorite::getCollectionId, collectionIds.get(i)));
            }
        }
        return count;
    }

    @Override
    public PageResult listFavoriteContent(@Nullable Long id, @NotNull Long favoriteId, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        if (!permissionService.checkFavoriteViewPermission(id, favoriteId)) {
            throw new PermissionDeniedException("权限拒绝访问操作");
        }
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<FavoriteObject> favoriteObjects = favoriteDao.listFavoriteContent(id, favoriteId);
        PageInfo<FavoriteObject> pageInfo = new PageInfo<>(favoriteObjects);
        List<Object> list = favoriteObjects.stream().map(favoriteObject -> {
            if (favoriteObject.getType()) {
                Note note = noteDao.selectById(favoriteObject.getId());
                return baseService.convert(id, note);
            }
            Collection collection = collectionDao.selectById(favoriteObject.getId());
            return baseService.convert(id, collection);
        }).toList();
        PageResult result = new PageResult(pageInfo, list);
        page.close();
        return result;
    }
}
