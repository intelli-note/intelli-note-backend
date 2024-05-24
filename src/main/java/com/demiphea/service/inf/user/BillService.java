package com.demiphea.service.inf.user;

import com.demiphea.model.api.PageResult;
import org.jetbrains.annotations.NotNull;

/**
 * BillService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface BillService {
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
}
