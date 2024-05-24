package com.demiphea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.dao.*;
import com.demiphea.entity.*;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.vo.note.NoteConfiguration;
import com.demiphea.model.vo.note.NoteOverviewState;
import com.demiphea.model.vo.note.NoteOverviewVo;
import com.demiphea.model.vo.user.BillVo;
import com.demiphea.model.vo.user.UserState;
import com.demiphea.model.vo.user.UserVo;
import com.demiphea.service.inf.BaseService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BaseServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class BaseServiceImpl implements BaseService {
    private final FollowDao followDao;
    private final UserDao userDao;
    private final ViewHistoryDao viewHistoryDao;
    private final NoteFavoriteDao noteFavoriteDao;
    private final CommentDao commentDao;
    private final FavoriteDao favoriteDao;
    private final NoteDao noteDao;

    @Override
    public UserVo convert(@NotNull User user) {
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
    public UserVo attachState(@NotNull Long id, @NotNull UserVo userVo) {
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

    @Override
    public NoteOverviewVo convert(@Nullable Long id, @NotNull Note note) {
        NoteOverviewVo noteOverviewVo = new NoteOverviewVo();
        noteOverviewVo.setId(note.getId());
        UserVo author = convert(userDao.selectById(note.getUserId()));
        if (id != null) {
            attachState(id, author);
        }
        noteOverviewVo.setAuthor(author);
        noteOverviewVo.setTitle(note.getTitle());
        noteOverviewVo.setCover(note.getCover());
        Long viewNum = viewHistoryDao.selectCount(new LambdaQueryWrapper<ViewHistory>().eq(ViewHistory::getNoteId, note.getId()));
        noteOverviewVo.setViewNum(viewNum.toString());
        Long starNum = noteFavoriteDao.selectCount(new LambdaQueryWrapper<NoteFavorite>().eq(NoteFavorite::getNoteId, note.getId()));
        noteOverviewVo.setStarNum(starNum.toString());
        Long commentNum = commentDao.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getNoteId, note.getId()));
        noteOverviewVo.setCommentNum(commentNum.toString());
        noteOverviewVo.setCreateTime(note.getCreateTime());
        noteOverviewVo.setUpdateTime(note.getUpdateTime());
        if (id != null) {
            boolean owner = id.equals(note.getUserId());
            noteOverviewVo.setState(new NoteOverviewState(
                    noteFavoriteDao.selectList(new LambdaQueryWrapper<NoteFavorite>().eq(NoteFavorite::getNoteId, note.getId())).stream()
                            .map(NoteFavorite::getFavoriteId)
                            .map(favoriteDao::selectById)
                            .map(Favorite::getUserId)
                            .distinct()
                            .anyMatch(x -> x.equals(id)),
                    owner
            ));
            if (owner) {
                noteOverviewVo.setConfiguration(new NoteConfiguration(
                        note.getOpenPublic(),
                        note.getPrice()
                ));
            }
        }
        return noteOverviewVo;
    }

    @Override
    public BillVo convert(@Nullable Long id, @NotNull Bill bill) {
        BillVo billVo = new BillVo();
        billVo.setId(bill.getId());
        billVo.setType(bill.getType());
        billVo.setAmount(bill.getAmount());
        Note note = noteDao.selectById(bill.getNoteId());
        billVo.setNote(convert(id, note));
        billVo.setCreateTime(bill.getCreateTime());
        return billVo;
    }

    @Override
    public PageResult convert(@NotNull PageInfo<?> pageInfo, @NotNull List<?> list) {
        PageResult result = new PageResult();
        result.setPages(pageInfo.getPages());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(list);
        result.setSize(pageInfo.getSize());
        result.setHasPreviousPage(pageInfo.isHasPreviousPage());
        result.setHasNextPage(pageInfo.isHasNextPage());
        result.setFirstPage(pageInfo.isIsFirstPage());
        result.setLastPage(pageInfo.isIsLastPage());
        result.setFirstPageNum(pageInfo.getNavigateFirstPage());
        result.setLastPageNum(pageInfo.getNavigateLastPage());
        return result;
    }
}
