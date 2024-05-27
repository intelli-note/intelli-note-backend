package com.demiphea.service.impl.collection;

import com.demiphea.common.Constant;
import com.demiphea.dao.CollectionDao;
import com.demiphea.entity.Collection;
import com.demiphea.exception.common.ObjectDoesNotExistException;
import com.demiphea.exception.common.PermissionDeniedException;
import com.demiphea.model.vo.collection.CollectionVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.collection.CollectionService;
import com.demiphea.utils.oss.qiniu.OssUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * CollectionServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    private final BaseService baseService;
    private final CollectionDao collectionDao;


    @Override
    public boolean checkAdminPermission(@Nullable Long id, @NotNull Long collectionId) {
        if (id == null) {
            return false;
        }
        Collection collection = collectionDao.selectById(collectionId);
        if (collection == null) {
            throw new ObjectDoesNotExistException("合集不存在或已删除");
        }
        return checkAdminPermission(id, collection);
    }

    @Override
    public boolean checkAdminPermission(@Nullable Long id, @NotNull Collection collection) {
        if (id == null) {
            return false;
        }
        return id.equals(collection.getUserId());
    }

    @Override
    public CollectionVo insertCollection(@NotNull Long id, @NotNull String name, @Nullable MultipartFile cover, @Nullable String description, @NotNull Boolean publicOption) throws IOException {
        String coverUrl = null;
        if (cover != null) {
            coverUrl = OssUtils.upload(Constant.COLLECTION_COVER_DIR, null, cover);
        }
        Collection collection = new Collection(
                null,
                name,
                coverUrl,
                description,
                id,
                LocalDateTime.now(),
                publicOption
        );
        collectionDao.insert(collection);
        return baseService.convert(id, collection);
    }

    @Override
    public CollectionVo updateCollection(@NotNull Long id, @NotNull Long collectionId, @Nullable String name, @Nullable MultipartFile cover, @Nullable String description, @Nullable Boolean publicOption) throws IOException {
        Collection collection = collectionDao.selectById(collectionId);
        if (collection == null) {
            throw new ObjectDoesNotExistException("合集不存在或已删除");
        }
        if (!checkAdminPermission(id, collection)) {
            throw new PermissionDeniedException("权限拒绝访问操作");
        }
        collection.setCname(name);
        String coverUrl = null;
        if (cover != null) {
            coverUrl = OssUtils.upload(Constant.COLLECTION_COVER_DIR, null, cover);
        }
        collection.setCover(coverUrl);
        collection.setBriefIntroduction(description);
        collection.setOpenPublic(publicOption);
        collectionDao.updateById(collection);
        return baseService.convert(id, collectionDao.selectById(collectionId));
    }

    @Override
    public void deleteCollection(@NotNull Long id, @NotNull Long collectionId) {
        if (!checkAdminPermission(id, collectionId)) {
            throw new PermissionDeniedException("权限拒绝访问操作");
        }
        collectionDao.deleteById(collectionId);
    }
}
