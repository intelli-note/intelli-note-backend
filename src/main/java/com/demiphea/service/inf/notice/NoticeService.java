package com.demiphea.service.inf.notice;

import com.demiphea.model.api.PageResult;
import com.demiphea.model.bo.notice.NoticeType;
import org.jetbrains.annotations.NotNull;

/**
 * NoticeService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface NoticeService {
    /**
     * 将通知设为已读状态
     *
     * @param id       当前用户ID
     * @param noticeId 通知ID
     * @author demiphea
     */
    void putRead(@NotNull Long id, @NotNull Long noticeId);

    /**
     * 删除通知
     *
     * @param id       当前用户ID
     * @param noticeId 通知ID
     * @author demiphea
     */
    void deleteNotice(@NotNull Long id, @NotNull Long noticeId);

    /**
     * 分页获取通知列表
     *
     * @param id       当前用户ID
     * @param type     通知类型
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return {@link PageResult} 结果
     * @author demiphea
     */
    PageResult listNotices(@NotNull Long id, @NotNull NoticeType type, @NotNull Integer pageNum, @NotNull Integer pageSize);
}
