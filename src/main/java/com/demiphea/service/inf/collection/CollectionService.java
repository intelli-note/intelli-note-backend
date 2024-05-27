package com.demiphea.service.inf.collection;

import com.demiphea.entity.Collection;
import com.demiphea.model.vo.collection.CollectionVo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * CollectionService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface CollectionService {
    /**
     * 校验管理权限
     *
     * @param id           当前用户ID
     * @param collectionId 合集ID
     * @return 是否有权限
     * @author demiphea
     */
    boolean checkAdminPermission(@Nullable Long id, @NotNull Long collectionId);

    /**
     * 校验管理权限
     *
     * @param id         当前用户ID
     * @param collection 合集
     * @return 是否有权限
     * @author demiphea
     */
    boolean checkAdminPermission(@Nullable Long id, @NotNull Collection collection);

    /**
     * 新建合集
     *
     * @param id           当前用户ID
     * @param name         合集名
     * @param cover        合集封面
     * @param description  合集简介
     * @param publicOption 合集是否公开
     * @return {@link CollectionVo} 合集VO
     * @author demiphea
     */
    CollectionVo insertCollection(@NotNull Long id, @NotNull String name, @Nullable MultipartFile cover, @Nullable String description, @NotNull Boolean publicOption) throws IOException;

    /**
     * 更改合集
     *
     * @param id           当前用户ID
     * @param collectionId 更改的合集ID
     * @param name         合集名
     * @param cover        合集封面
     * @param description  合集简介
     * @param publicOption 合集是否公开
     * @return {@link CollectionVo} 合集VO
     * @author demiphea
     */
    CollectionVo updateCollection(@NotNull Long id, @NotNull Long collectionId, @Nullable String name, @Nullable MultipartFile cover, @Nullable String description, @Nullable Boolean publicOption) throws IOException;

    /**
     * 删除合集
     *
     * @param id           当前用户ID
     * @param collectionId 合集ID
     * @author demiphea
     */
    void deleteCollection(@NotNull Long id, @NotNull Long collectionId);
}
