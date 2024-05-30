package com.demiphea.service.impl;

import com.alibaba.fastjson2.JSON;
import com.demiphea.dao.CommentDao;
import com.demiphea.entity.Comment;
import com.demiphea.exception.common.CommonServiceException;
import com.demiphea.model.dto.comment.CommentDto;
import com.demiphea.model.vo.comment.CommentVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.PermissionService;
import com.demiphea.service.inf.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
