package com.demiphea.service.impl.collection;

import com.demiphea.common.Constant;
import com.demiphea.dao.CollectionDao;
import com.demiphea.entity.Collection;
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
    public CollectionVo insertCollection(@NotNull Long id, @NotNull String name, @Nullable MultipartFile cover, @Nullable String description, @NotNull Boolean publicOption) throws IOException {
        String coverUrl = null;
        if (cover != null) {
            coverUrl = OssUtils.upload(Constant.COLLECTION_COVER_DIR, null, cover);
        }
        Collection collection = new Collection(
                id,
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
}
