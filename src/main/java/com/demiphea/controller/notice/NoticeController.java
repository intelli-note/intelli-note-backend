package com.demiphea.controller.notice;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.bo.notice.NoticeType;
import com.demiphea.service.inf.notice.NoticeService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * NoticeController
 *
 * @author demiphea
 * @since 17.0.9
 */
@Validated
@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PutMapping("/{noticeId}")
    @Auth
    public ApiResponse putRead(@AuthID Long id, @PathVariable Long noticeId) {
        noticeService.putRead(id, noticeId);
        return ApiResponse.success();
    }

    @DeleteMapping("/{noticeId}")
    @Auth
    public ApiResponse deleteNotice(@AuthID Long id, @PathVariable Long noticeId) {
        noticeService.deleteNotice(id, noticeId);
        return ApiResponse.success();
    }

    @GetMapping
    @Auth
    public ApiResponse listNotices(
            @AuthID Long id,
            @RequestParam("type")
            String noticeType,
            @RequestParam("page_num")
            @Min(value = 1, message = "页码需要从1开始")
            Integer pageNum,
            @RequestParam("page_size")
            @Min(value = 1, message = "每页数量必须大于1")
            Integer pageSize
    ) {
        NoticeType type;
        try {
            type = NoticeType.valueOf(noticeType.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("通知类型参数传入错误");
        }
        PageResult result = noticeService.listNotices(id, type, pageNum, pageSize);
        return ApiResponse.success(result);
    }
}
