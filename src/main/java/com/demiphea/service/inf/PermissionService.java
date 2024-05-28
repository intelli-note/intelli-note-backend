package com.demiphea.service.inf;

import com.demiphea.entity.Bill;
import com.demiphea.entity.Collection;
import com.demiphea.entity.Favorite;
import com.demiphea.entity.Note;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * PermissionService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface PermissionService {
    /**
     * 权限校验-查看用户是否有资格操作账单
     *
     * @param userId 用户ID
     * @param billId 账单ID
     * @return 是否有权限
     * @author demiphea
     */
    boolean checkBillAdminPermission(@Nullable Long userId, @NotNull Long billId);

    /**
     * 权限校验-查看用户是否有资格操作账单
     *
     * @param userId 用户ID
     * @param bill   账单
     * @return 是否有权限
     * @author demiphea
     */
    boolean checkBillAdminPermission(@Nullable Long userId, @NotNull Bill bill);


    /**
     * 校验是否具有笔记的管理权限
     *
     * @param id     用户ID
     * @param noteId 笔记ID
     * @return 是否具有管理权限
     * @author demiphea
     */
    boolean checkNoteAdminPermission(@Nullable Long id, @NotNull Long noteId);

    /**
     * 校验是否具有笔记的管理权限
     *
     * @param id   用户ID
     * @param note 笔记
     * @return 是否具有管理权限
     * @author demiphea
     */
    boolean checkNoteAdminPermission(@Nullable Long id, @NotNull Note note);

    /**
     * 校验是否具有笔记的阅读权限
     *
     * @param id     用户ID
     * @param noteId 笔记ID
     * @return 是否具有阅读权限
     * @author demiphea
     */
    boolean checkNoteReadPermission(@Nullable Long id, @NotNull Long noteId);

    /**
     * 校验是否具有笔记的阅读权限
     *
     * @param id   用户ID
     * @param note 笔记
     * @return 是否具有阅读权限
     * @author demiphea
     */
    boolean checkNoteReadPermission(@Nullable Long id, @NotNull Note note);

    /**
     * 校验合集管理权限
     *
     * @param id           当前用户ID
     * @param collectionId 合集ID
     * @return 是否有权限
     * @author demiphea
     */
    boolean checkCollectionAdminPermission(@Nullable Long id, @NotNull Long collectionId);

    /**
     * 校验合集管理权限
     *
     * @param id         当前用户ID
     * @param collection 合集
     * @return 是否有权限
     * @author demiphea
     */
    boolean checkCollectionAdminPermission(@Nullable Long id, @NotNull Collection collection);

    /**
     * 校验收藏夹管理权限
     *
     * @param id         当前用户ID
     * @param favoriteId 收藏夹ID
     * @return 是否有权限
     * @author demiphea
     */
    boolean checkFavoriteAdminPermission(@Nullable Long id, @NotNull Long favoriteId);

    /**
     * 校验收藏夹管理权限
     *
     * @param id       当前用户ID
     * @param favorite 收藏夹
     * @return 是否有权限
     * @author demiphea
     */
    boolean checkFavoriteAdminPermission(@Nullable Long id, @NotNull Favorite favorite);

    /**
     * 校验收藏夹阅读权限
     *
     * @param id         当前用户ID
     * @param favoriteId 收藏夹ID
     * @return 是否有权限
     * @author demiphea
     */
    boolean checkFavoriteViewPermission(@Nullable Long id, @NotNull Long favoriteId);

    /**
     * 校验收藏夹阅读权限
     *
     * @param id       当前用户ID
     * @param favorite 收藏夹
     * @return 是否有权限
     * @author demiphea
     */
    boolean checkFavoriteViewPermission(@Nullable Long id, @NotNull Favorite favorite);

}
