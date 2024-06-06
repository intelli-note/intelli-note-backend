package com.demiphea.service.impl.comment;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.core.common.CommentSort;
import com.demiphea.dao.CommentDao;
import com.demiphea.dao.CommentLikeDao;
import com.demiphea.entity.Comment;
import com.demiphea.entity.CommentLike;
import com.demiphea.exception.common.CommonServiceException;
import com.demiphea.exception.common.PermissionDeniedException;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.bo.notice.NoticeType;
import com.demiphea.model.dto.comment.CommentDto;
import com.demiphea.model.vo.comment.CommentVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.MessageQueueService;
import com.demiphea.service.inf.PermissionService;
import com.demiphea.service.inf.SystemService;
import com.demiphea.service.inf.comment.CommentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * CommentServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final BaseService baseService;
    private final PermissionService permissionService;
    private final CommentDao commentDao;
    private final CommentLikeDao commentLikeDao;
    private final MessageQueueService messageQueueService;
    private final SystemService systemService;

    private final Comparator<CommentVo> comparator = (a, b) -> {
        BigDecimal aScore = CommentSort.calcScore(a.getAgreeNumber(), a.getReplyNumber(), a.getCreateTime());
        BigDecimal bScore = CommentSort.calcScore(b.getAgreeNumber(), b.getReplyNumber(), b.getCreateTime());
        return bScore.compareTo(aScore);
    };

    private String getSimpleText(CommentDto commentDto) {
        String result = "";
        if (commentDto.getText() != null) {
            result += commentDto.getText();
        }
        if (commentDto.getImageList() != null && !commentDto.getImageList().isEmpty()) {
            result += "[图片]";
        }
        if (commentDto.getAudio() != null) {
            result += "[音频]";
        }
        if (commentDto.getVideo() != null) {
            result += "[视频]";
        }
        if (commentDto.getLinkNoteId() != null) {
            result += "[笔记]";
        }
        return result;
    }

    @Override
    public CommentVo insertComment(@NotNull Long id, @NotNull CommentDto commentDto) {
        String imageList = commentDto.getImageList() != null ? JSON.toJSONString(commentDto.getImageList()) : null;
        Comment comment = new Comment(
                null,

                commentDto.getText(),
                imageList,
                commentDto.getAudio(),
                commentDto.getVideo(),
                commentDto.getLinkNoteId(),

                commentDto.getNoteId(),
                commentDto.getRootId(),
                commentDto.getParentId(),
                id,
                LocalDateTime.now(),

                0L,
                0L,
                getSimpleText(commentDto)
        );
        try {
            commentDao.insert(comment);
        } catch (DataIntegrityViolationException e) {
            throw new CommonServiceException("关联笔记不存在");
        }
        if (comment.getParentId() != null) {
            // 刷新父评论的回复数
            messageQueueService.refreshComment(comment.getParentId());
        }
        systemService.publishNotice(NoticeType.COMMENT, comment.getId());
        return baseService.convert(id, comment);
    }

    @Override
    public void deleteComment(@NotNull Long id, @NotNull Long commentId) {
        Comment comment = commentDao.selectById(commentId);
        if (comment == null) {
            return;
        }
        if (!permissionService.checkCommentAdminPermission(id, comment)) {
            throw new PermissionDeniedException("权限拒绝操作");
        }
        commentDao.deleteById(comment);
        if (comment.getParentId() != null) {
            // 刷新父评论的回复数
            messageQueueService.refreshComment(comment.getParentId());
        }
    }

    @Override
    public PageResult listFirstComments(@Nullable Long id, @NotNull Long noteId, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Comment> comments = commentDao.selectList(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getNoteId, noteId)
                .isNull(Comment::getRootId)
                .isNull(Comment::getParentId)
                .orderByDesc(List.of(Comment::getAgreeNum, Comment::getReplyNum, Comment::getCreateTime))
        );
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);
        List<CommentVo> list = comments.stream()
                .map(comment -> baseService.convert(id, comment))
                .sorted(comparator)
                .toList();
        PageResult result = new PageResult(pageInfo, list);
        page.close();
        return result;
    }

    @Override
    public PageResult listSecondComments(@Nullable Long id, @NotNull Long rootId, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Comment> comments = commentDao.selectList(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getRootId, rootId)
                .orderByDesc(List.of(Comment::getAgreeNum, Comment::getReplyNum, Comment::getCreateTime))
        );
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);
        List<CommentVo> list = comments.stream()
                .map(comment -> baseService.convert(id, comment))
                .sorted(comparator)
                .toList();
        PageResult result = new PageResult(pageInfo, list);
        page.close();
        return result;
    }

    @Override
    public void upVote(@NotNull Long id, @NotNull Long commentId) {
        try {
            CommentLike entity = new CommentLike(
                    null,
                    commentId,
                    id,
                    LocalDateTime.now()
            );
            commentLikeDao.insert(entity);
            messageQueueService.refreshComment(commentId);
            systemService.publishNotice(NoticeType.LIKE, entity.getId());
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void downVote(@NotNull Long id, @NotNull Long commentId) {
        commentLikeDao.delete(new LambdaQueryWrapper<CommentLike>()
                .eq(CommentLike::getCommentId, commentId)
                .eq(CommentLike::getUserId, id)
        );
        messageQueueService.refreshComment(commentId);
    }
}
