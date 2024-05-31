package com.demiphea.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.core.CommentComparator;
import com.demiphea.dao.CommentDao;
import com.demiphea.entity.Comment;
import com.demiphea.exception.common.CommonServiceException;
import com.demiphea.exception.common.PermissionDeniedException;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.dto.comment.CommentDto;
import com.demiphea.model.vo.comment.CommentVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.PermissionService;
import com.demiphea.service.inf.comment.CommentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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

    private final Comparator<CommentVo> comparator = new CommentComparator();

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
                LocalDateTime.now()
        );
        try {
            commentDao.insert(comment);
        } catch (DataIntegrityViolationException e) {
            throw new CommonServiceException("关联笔记不存在");
        }
        return baseService.convert(id, comment);
    }

    @Override
    public void deleteComment(@NotNull Long id, @NotNull Long commentId) {
        if (!permissionService.checkCommentAdminPermission(id, commentId)) {
            throw new PermissionDeniedException("权限拒绝操作");
        }
        commentDao.deleteById(commentId);
    }

    @Override
    public PageResult listFirstComments(@Nullable Long id, @NotNull Long noteId, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Comment> comments = commentDao.selectList(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getNoteId, noteId)
                .isNull(Comment::getRootId)
                .isNull(Comment::getParentId)
                .orderByDesc(Comment::getCreateTime));
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);
        List<CommentVo> list = comments.stream()
                .map(comment -> baseService.convert(id, comment))
                .sorted(comparator)
                .toList();
        PageResult result = new PageResult(pageInfo, list);
        page.close();
        return result;
    }
}
