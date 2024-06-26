package com.demiphea.controller.comment;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.dto.comment.CommentDto;
import com.demiphea.model.vo.comment.CommentVo;
import com.demiphea.service.inf.comment.CommentService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * CommentController
 *
 * @author demiphea
 * @since 17.0.9
 */
@Validated
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @Auth
    public ApiResponse insertComment(@AuthID Long id, @RequestBody @Validated CommentDto commentDto) {
        CommentVo commentVo = commentService.insertComment(id, commentDto);
        return ApiResponse.success(commentVo);
    }

    @DeleteMapping("/{commentId}")
    @Auth
    public ApiResponse deleteComment(@AuthID Long id, @PathVariable Long commentId) {
        commentService.deleteComment(id, commentId);
        return ApiResponse.success();
    }

    @GetMapping
    @Auth(block = false)
    public ApiResponse listComments(
            @AuthID
            Long id,
            @RequestParam(value = "note_id", required = false)
            Long noteId,
            @RequestParam(value = "root_id", required = false)
            Long rootId,
            @RequestParam("page_num")
            @Min(value = 1, message = "页码需要从1开始")
            Integer pageNum,
            @RequestParam("page_size")
            @Min(value = 1, message = "每页数量必须大于1")
            Integer pageSize
    ) {
        PageResult result;
        if (rootId != null) {
            result = commentService.listSecondComments(id, rootId, pageNum, pageSize);
        } else if (noteId != null) {
            result = commentService.listFirstComments(id, noteId, pageNum, pageSize);
        } else {
            result = PageResult.EMPTY;
        }
        return ApiResponse.success(result);
    }

    @PostMapping("/like/{commentId}")
    @Auth
    public ApiResponse upVote(@AuthID Long id, @PathVariable Long commentId) {
        commentService.upVote(id, commentId);
        return ApiResponse.success();
    }

    @DeleteMapping("/like/{commentId}")
    @Auth
    public ApiResponse downVote(@AuthID Long id, @PathVariable Long commentId) {
        commentService.downVote(id, commentId);
        return ApiResponse.success();
    }
}
