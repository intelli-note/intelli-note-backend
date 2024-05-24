package com.demiphea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.dao.FollowDao;
import com.demiphea.dao.UserDao;
import com.demiphea.entity.Follow;
import com.demiphea.entity.User;
import com.demiphea.model.vo.user.UserState;
import com.demiphea.model.vo.user.UserVo;
import com.demiphea.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * BaseServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class BaseServiceImpl implements BaseService {
    private final UserDao userDao;
    private final FollowDao followDao;

    @Override
    public UserVo convert(Long id) {
        User user = userDao.selectById(id);
        return convert(user);
    }

    @Override
    public UserVo convert(User user) {
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setUsername(user.getUsername());
        userVo.setAvatar(user.getAvatar());
        userVo.setBiography(user.getBiography());
        Boolean gender = user.getGender();
        userVo.setGender(gender == null ? "未知" : gender ? "男" : "女");
        userVo.setFollowsNum(followDao.selectCount(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, user.getId())).toString());
        userVo.setFollowersNum(followDao.selectCount(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowId, user.getId())).toString());
        return userVo;
    }

    @Override
    public UserVo attachState(Long id, UserVo userVo) {
        UserState state = new UserState();
        state.setSelfStatus(id.equals(userVo.getId()));
        UserState.FollowStatus followStatus = UserState.FollowStatus.UNFOLLOWED;
        boolean follow = followDao.exists(new LambdaQueryWrapper<Follow>()
                .eq(Follow::getFollowerId, id).eq(Follow::getFollowId, userVo.getId())
        );
        if (follow) {
            boolean followed = followDao.exists(new LambdaQueryWrapper<Follow>()
                    .eq(Follow::getFollowerId, userVo.getId()).eq(Follow::getFollowId, id)
            );
            followStatus = !followed ? UserState.FollowStatus.FOLLOWED : UserState.FollowStatus.FOLLOW_EACH_OTHER;
        }
        state.setFollowStatus(followStatus);

        userVo.setState(state);
        return userVo;
    }
}
