package com.demiphea.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.dao.FollowDao;
import com.demiphea.dao.UserDao;
import com.demiphea.entity.Follow;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.vo.user.UserVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.user.FollowService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * FollowServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final BaseService baseService;
    private final FollowDao followDao;
    private final UserDao userDao;

    @Override
    public void follow(@NotNull Long id, @NotNull Long targetId) {
        Follow follow = new Follow(null, id, targetId, LocalDateTime.now());
        try {
            followDao.insert(follow);
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void unfollow(@NotNull Long id, @NotNull Long targetId) {
        followDao.delete(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, id).eq(Follow::getFollowId, targetId));
    }

    @Override
    public PageResult listFollows(@NotNull Long id, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Follow> follows = followDao.selectList(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, id));
        PageInfo<Follow> pageInfo = new PageInfo<>(follows);
        List<UserVo> list = follows.stream()
                .map(Follow::getFollowId)
                .map(userDao::selectById)
                .map(baseService::convert)
                .map(vo -> baseService.attachState(id, vo))
                .toList();
        PageResult result = baseService.convert(pageInfo, list);
        page.close();
        return result;
    }

    @Override
    public PageResult listFollowers(@NotNull Long id, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Follow> followers = followDao.selectList(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowId, id));
        PageInfo<Follow> pageInfo = new PageInfo<>(followers);
        List<UserVo> list = followers.stream()
                .map(Follow::getFollowerId)
                .map(userDao::selectById)
                .map(baseService::convert)
                .map(vo -> baseService.attachState(id, vo))
                .toList();
        PageResult result = baseService.convert(pageInfo, list);
        page.close();
        return result;
    }
}
