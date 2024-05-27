package com.demiphea.service.inf.user;

import com.demiphea.entity.Bill;
import com.demiphea.model.api.PageResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * BillService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface BillService {
    /**
     * 权限校验-查看用户是否有资格操作
     *
     * @param userId 用户ID
     * @param billId 账单ID
     * @return {@link Boolean} 是否有权限
     * @author demiphea
     */
    Boolean checkPermission(@Nullable Long userId, @NotNull Long billId);

    /**
     * 权限校验-查看用户是否有资格操作
     *
     * @param userId 用户ID
     * @param bill   账单
     * @return {@link Boolean} 是否有权限
     * @author demiphea
     */
    Boolean checkPermission(@Nullable Long userId, @NotNull Bill bill);

    /**
     * 分页获取用户账单列表
     *
     * @param id       当前用户ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return {@link PageResult} 分页结果
     * @author demiphea
     */
    PageResult listBills(@NotNull Long id, @NotNull Integer pageNum, @NotNull Integer pageSize);

    /**
     * 删除用户账单（逻辑删除）
     *
     * @param id     当前用户ID
     * @param billId 账单ID
     * @author demiphea
     */
    void deleteBill(@NotNull Long id, @NotNull Long billId);
}
