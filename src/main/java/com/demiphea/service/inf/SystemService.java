package com.demiphea.service.inf;

import com.demiphea.model.bo.notice.NoticeType;
import com.demiphea.model.bo.user.BillType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * SystemService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface SystemService {
    /**
     * 插入用户账单（系统调用）
     *
     * @param userId 用户ID
     * @param type   账单类型
     * @param amount 金额
     * @param noteId 笔记ID
     * @author demiphea
     */
    void insertBill(@NotNull Long userId, @NotNull BillType type, @NotNull BigDecimal amount, @Nullable Long noteId);

    /**
     * 发送通知（系统调用）
     *
     * @param type   通知类型（不支持{@link NoticeType#ALL}）
     * @param linkId 关联ID
     * @param userId 用户ID
     * @author demiphea
     */
    void publishNotice(@NotNull NoticeType type, @NotNull Long linkId, @NotNull Long userId);
}
