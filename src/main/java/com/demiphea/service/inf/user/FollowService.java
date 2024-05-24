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

    /**
     * 分页获取用户关注列表
     *
     * @param id       当前用户ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return {@link PageResult} 分页结果
     * @author demiphea
     */
    PageResult listFollows(@NotNull Long id, @NotNull Integer pageNum, @NotNull Integer pageSize);

    /**
     * 分页获取用户粉丝列表
     * @param id 当前用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return {@link PageResult} 分页结果
     * @author demiphea
     */
    PageResult listFollowers(@NotNull Long id, @NotNull Integer pageNum, @NotNull Integer pageSize);
}
