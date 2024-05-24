package com.demiphea.service.inf.user;

import com.demiphea.model.api.PageResult;
import org.jetbrains.annotations.NotNull;

/**
 * FollowService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface FollowService {
    /**
     * 关注用户
     *
     * @param id       当前用户ID
     * @param targetId 目标用户ID
     * @author demiphea
     */
    void follow(@NotNull Long id, @NotNull Long targetId);

    /**
     * 取消关注用户
     *
     * @param id       当前用户ID
     * @param targetId 目标用户ID
     * @author demiphea
     */
    void unfollow(@NotNull Long id, @NotNull Long targetId);

    PageResult listFollows(@NotNull Long id, @NotNull Integer pageNum, @NotNull Integer pageSize);

    PageResult listFollowers(@NotNull Long id, @NotNull Integer pageNum, @NotNull Integer pageSize);
}
