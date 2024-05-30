package com.demiphea.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.dao.*;
import com.demiphea.entity.*;
import com.demiphea.model.vo.collection.CollectionConfiguration;
import com.demiphea.model.vo.collection.CollectionState;
import com.demiphea.model.vo.collection.CollectionVo;
import com.demiphea.model.vo.comment.CommentState;
import com.demiphea.model.vo.comment.CommentVo;
import com.demiphea.model.vo.favorite.FavoriteConfiguration;
import com.demiphea.model.vo.favorite.FavoriteState;
import com.demiphea.model.vo.favorite.FavoriteVo;
import com.demiphea.model.vo.note.*;
import com.demiphea.model.vo.user.BillVo;
import com.demiphea.model.vo.user.UserState;
import com.demiphea.model.vo.user.UserVo;
import com.demiphea.service.inf.BaseService;
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
    private final CollectionFavoriteDao collectionFavoriteDao;
    private final NoteCollectDao noteCollectDao;
    private final CommentLikeDao commentLikeDao;

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
            noteOverviewVo.setState(new NoteOverviewState(
                    noteFavoriteDao.selectList(new LambdaQueryWrapper<NoteFavorite>().eq(NoteFavorite::getNoteId, note.getId())).stream()
                            .map(NoteFavorite::getFavoriteId)
                            .map(favoriteDao::selectById)
                            .map(Favorite::getUserId)
                            .distinct()
                            .anyMatch(x -> x.equals(id)),
                    id.equals(note.getUserId())
            ));
        }
        noteOverviewVo.setConfiguration(new NoteConfiguration(
                note.getOpenPublic(),
                note.getPrice()
        ));
        return noteOverviewVo;
    }

    @Override
    public CommentVo convert(@Nullable Long id, @NotNull Comment comment) {
        CommentVo commentVo = new CommentVo();
        commentVo.setId(comment.getId());
        UserVo user = convert(userDao.selectById(comment.getUserId()));
        if (id != null) {
            attachState(id, user);
        }
        commentVo.setUser(user);
        commentVo.setText(comment.getContent());
        commentVo.setImageList(JSON.parseArray(comment.getImageList(), String.class));
        commentVo.setAudio(comment.getAudio());
        commentVo.setVideo(comment.getVideo());
        if (comment.getLinkNoteId() != null) {
            Note note = noteDao.selectById(comment.getLinkNoteId());
            if (note != null) {
                commentVo.setNote(convert(id, note));
            } else {
                // 表示引用笔记已删除
                commentVo.setNote(new NoteOverviewVo());
            }
        }
        Long likeCount = commentLikeDao.selectCount(new LambdaQueryWrapper<CommentLike>().eq(CommentLike::getCommentId, comment.getId()));
        commentVo.setAgreeNum(likeCount.toString());
        Long replyCount = commentDao.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getParentId, comment.getId()));
        commentVo.setReplyNum(replyCount.toString());
        commentVo.setCreateTime(comment.getCreateTime());
        if (id != null) {
            boolean agreeStatus = commentLikeDao.exists(new LambdaQueryWrapper<CommentLike>()
                    .eq(CommentLike::getCommentId, comment.getId())
                    .eq(CommentLike::getUserId, id));
            commentVo.setState(new CommentState(agreeStatus));
        }
        return commentVo;
    }

    @Override
    public NoteVo pack(@Nullable Long id, @NotNull Note note) {
        NoteVo noteVo = new NoteVo();
        noteVo.setOverview(convert(id, note));
        noteVo.setContent(note.getContent());
        if (id != null) {
            List<FavoriteVo> list = noteFavoriteDao.selectList(new LambdaQueryWrapper<NoteFavorite>().eq(NoteFavorite::getNoteId, note.getId())).stream()
                    .map(NoteFavorite::getFavoriteId)
                    .map(favoriteDao::selectById)
                    .filter(favorite -> id.equals(favorite.getUserId()))
                    .map(favorite -> convert(id, favorite))
                    .toList();
            noteVo.setState(new NoteState(list));
        }
        return noteVo;
    }

    @Override
    public BillVo convert(@Nullable Long id, @NotNull Bill bill) {
        BillVo billVo = new BillVo();
        billVo.setId(bill.getId());
        billVo.setType(bill.getType());
        billVo.setAmount(bill.getAmount());
        if (bill.getNoteId() != null) {
            Note note = noteDao.selectById(bill.getNoteId());
            billVo.setNote(convert(id, note));
        }
        billVo.setCreateTime(bill.getCreateTime());
        return billVo;
    }

    @Override
    public FavoriteVo convert(@Nullable Long id, @NotNull Favorite favorite) {
        FavoriteVo favoriteVo = new FavoriteVo();
        favoriteVo.setId(favorite.getId());
        favoriteVo.setName(favorite.getFname());
        favoriteVo.setDescription(favorite.getBriefIntroduction());
        Long noteCount = noteFavoriteDao.selectCount(new LambdaQueryWrapper<NoteFavorite>().eq(NoteFavorite::getFavoriteId, favorite.getId()));
        Long collectionCount = collectionFavoriteDao.selectCount(new LambdaQueryWrapper<CollectionFavorite>().eq(CollectionFavorite::getFavoriteId, favorite.getId()));
        favoriteVo.setNum((int) (noteCount + collectionCount));
        favoriteVo.setCreateTime(favorite.getCreateTime());
        if (id != null) {
            favoriteVo.setState(new FavoriteState(id.equals(favorite.getUserId())));
        }
        favoriteVo.setConfiguration(new FavoriteConfiguration(
                favorite.getOptionPublic(),
                favorite.getOptionDefault()
        ));
        return favoriteVo;
    }

    @Override
    public CollectionVo convert(@Nullable Long id, @NotNull Collection collection) {
        CollectionVo collectionVo = new CollectionVo();
        collectionVo.setId(collection.getId());
        collectionVo.setName(collection.getCname());
        collectionVo.setCover(collection.getCover());
        collectionVo.setDescription(collection.getBriefIntroduction());
        Long num = noteCollectDao.selectCount(new LambdaQueryWrapper<NoteCollect>().eq(NoteCollect::getCollectionId, collection.getId()));
        collectionVo.setNum(num.intValue());
        Long starNum = collectionFavoriteDao.selectCount(new LambdaQueryWrapper<CollectionFavorite>().eq(CollectionFavorite::getCollectionId, collection.getId()));
        collectionVo.setStarNum(starNum.toString());
        collectionVo.setCreateTime(collection.getCreateTime());
        if (id != null) {
            CollectionState state = new CollectionState();
            List<FavoriteVo> list = collectionFavoriteDao.selectList(new LambdaQueryWrapper<CollectionFavorite>().eq(CollectionFavorite::getCollectionId, collection.getId())).stream()
                    .map(CollectionFavorite::getFavoriteId)
                    .map(favoriteDao::selectById)
                    .filter(favorite -> id.equals(favorite.getUserId()))
                    .distinct()
                    .map(favorite -> convert(id, favorite))
                    .toList();
            state.setStar(!list.isEmpty());
            state.setOwner(id.equals(collection.getUserId()));
            state.setFavoriteList(list);
            collectionVo.setState(state);
        }
        collectionVo.setConfiguration(new CollectionConfiguration(collection.getOpenPublic()));
        return collectionVo;
    }
}
