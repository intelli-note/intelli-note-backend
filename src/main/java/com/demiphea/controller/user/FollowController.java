package com.demiphea.controller.user;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.po.user.FollowPo;
import com.demiphea.service.inf.user.FollowService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * FollowController
 *
 * @author demiphea
 * @since 17.0.9
 */
@Validated
@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping
    @Auth
    public ApiResponse follow(@AuthID Long id, @RequestBody @Validated FollowPo followPo) {
        followService.follow(id, followPo.getTargetId());
        return ApiResponse.success();
    }

    @DeleteMapping("/{targetId}")
    @Auth
    public ApiResponse unfollow(@AuthID Long id, @PathVariable @NotNull(message = "未传入关注用户") Long targetId) {
        followService.unfollow(id, targetId);
        return ApiResponse.success();
    }

    @GetMapping
    @Auth
    public ApiResponse listFollows(
            @AuthID
            Long id,
            @RequestParam("page_num")
            @Min(value = 1, message = "页码需要从1开始")
            Integer pageNum,
            @RequestParam("page_size")
            @Min(value = 1, message = "每页数量必须大于1")
            Integer pageSize
    ) {
        PageResult result = followService.listFollows(id, pageNum, pageSize);
        return ApiResponse.success(result);
    }
}
