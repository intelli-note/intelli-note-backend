package com.demiphea.core.mq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.demiphea.dao.*;
import com.demiphea.entity.*;
import com.demiphea.model.bo.notice.NoticeBo;
import com.demiphea.model.dto.favorite.FavoriteCloneDto;
import com.demiphea.service.inf.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ServiceListener
 *
 * @author demiphea
 * @since 17.0.9
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceListener {
    public static final String EXCHANGE_NAME = "service_module";

    private final ViewHistoryDao viewHistoryDao;
    private final PermissionService permissionService;
    private final NoteFavoriteDao noteFavoriteDao;
    private final CollectionFavoriteDao collectionFavoriteDao;
    private final CommentDao commentDao;
    private final CommentLikeDao commentLikeDao;
    private final NoticeDao noticeDao;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("notice::publish"),
            exchange = @Exchange(value = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = "notice.publish"
    ))
    public void publishNotice(@NotNull NoticeBo noticeBo) {
        Notice notice = new Notice();
        notice.setType(noticeBo.getType().type);
        switch (noticeBo.getType()) {
            case FOLLOW -> notice.setLinkFollowId(noticeBo.getLinkId());
            case NOTE_STAR -> notice.setLinkStarNoteId(noticeBo.getLinkId());
            case COLLECTION_STAR -> notice.setLinkStarCollectionId(noticeBo.getLinkId());
            case COMMENT -> notice.setLinkCommentId(noticeBo.getLinkId());
            case LIKE -> notice.setLinkCommentLikeId(noticeBo.getLinkId());
            case TRADE -> notice.setLinkBillId(noticeBo.getLinkId());
//            case ALL -> throw new CommonServiceException("不支持的通知类别");
            case ALL -> {
                return;
            }
        }
        notice.setUserId(noticeBo.getUserId());
        notice.setCreateTime(LocalDateTime.now());
        notice.setRead(false);
        try {
            noticeDao.insert(notice);
        } catch (Exception e) {
            // ignore
        }

        // TODO: websocket发送消息

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("view_history::save"),
            exchange = @Exchange(value = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = "view_history.save"
    ))
    public void saveViewHistory(@NotNull ViewHistory viewHistory) {
        try {
            viewHistoryDao.insert(viewHistory);
        } catch (Exception e) {
            // ignore
        }
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("favorite::clone"),
            exchange = @Exchange(value = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = "favorite.clone"
    ))
    public void cloneFavorite(@NotNull FavoriteCloneDto favoriteCloneDto) {
        Long userId = favoriteCloneDto.getUserId();
        Long sourceId = favoriteCloneDto.getSourceId();
        Long distId = favoriteCloneDto.getDistId();

        if (!permissionService.checkFavoriteViewPermission(userId, sourceId)) {
            return;
        }
        List<Long> noteIds = noteFavoriteDao.selectList(new LambdaQueryWrapper<NoteFavorite>()
                .eq(NoteFavorite::getFavoriteId, sourceId)
        ).stream().map(NoteFavorite::getNoteId).toList();
        for (Long noteId : noteIds) {
            try {
                noteFavoriteDao.insert(new NoteFavorite(null, noteId, distId, LocalDateTime.now()));
            } catch (Exception e) {
                // ignore
            }
        }
        List<Long> collectionIds = collectionFavoriteDao.selectList(new LambdaQueryWrapper<CollectionFavorite>()
                .eq(CollectionFavorite::getFavoriteId, sourceId)
        ).stream().map(CollectionFavorite::getCollectionId).toList();
        for (Long collectionId : collectionIds) {
            try {
                collectionFavoriteDao.insert(new CollectionFavorite(null, collectionId, distId, LocalDateTime.now()));
            } catch (Exception e) {
                // ignore
            }
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("comment::refresh"),
            exchange = @Exchange(value = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = "comment.refresh"
    ))
    public void refreshComment(@NotNull Long commentId) {
        Long replyNum = commentDao.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getParentId, commentId)
        );
        commentDao.update(new LambdaUpdateWrapper<Comment>()
                .eq(Comment::getId, commentId)
                .set(Comment::getReplyNum, replyNum)
        );
        Long agreeNum = commentLikeDao.selectCount(new LambdaQueryWrapper<CommentLike>()
                .eq(CommentLike::getCommentId, commentId)
        );
        commentDao.update(new LambdaUpdateWrapper<Comment>()
                .eq(Comment::getId, commentId)
                .set(Comment::getAgreeNum, agreeNum)
        );
    }

}
