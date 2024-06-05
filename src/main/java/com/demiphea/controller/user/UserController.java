package com.demiphea.controller.user;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.vo.user.Credential;
import com.demiphea.model.vo.user.UserVo;
import com.demiphea.service.inf.user.UserService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.jetbrains.annotations.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * UserController
 *
 * @author demiphea
 * @since 17.0.9
 */
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/licence")
    public ApiResponse licence(
            @RequestParam
            @NotBlank(message = "登录凭证不能为空")
            String code
    ) throws URISyntaxException, IOException, ParseException {
        Credential credential = userService.licence(code);
        return ApiResponse.success(credential);
    }

    @PostMapping
    @Auth
    public ApiResponse updateUserProfile(
            @AuthID
            Long id,
            @RequestParam(required = false)
            @Length(min = 1, max = 20, message = "用户名长度为1~20个字符")
            String username,
            @RequestParam(required = false)
            MultipartFile avatar,
            @RequestParam(required = false)
            @Length(max = 250, message = "个人简介长度为0~250个字符")
            String biography,
            @RequestParam(required = false)
            @Range(min = 0, max = 2, message = "性别枚举值: 0(未知), 1(男), 2(女)")
            Integer gender
    ) throws IOException {
        UserVo userVo = userService.updateUserProfile(id, username, avatar, biography, gender);
        return ApiResponse.success(userVo);
    }

    @Auth(block = false)
    @GetMapping("/{targetId}")
    public ApiResponse getUserProfile(
            @AuthID
            Long id,
            @PathVariable
            @NotNull
            Long targetId
    ) {
        UserVo targetVo = userService.getUserProfile(id, targetId);
        return ApiResponse.success(targetVo);
    }

    @GetMapping
    @Auth(block = false)
    public ApiResponse searchUsers(
            @AuthID
            @Nullable
            Long id,
            @RequestParam(value = "key", required = false)
            String key,
            @RequestParam("page_num")
            @Min(value = 1, message = "页码需要从1开始")
            Integer pageNum,
            @RequestParam("page_size")
            @Min(value = 1, message = "每页数量必须大于1")
            Integer pageSize
    ) {
        PageResult result = userService.searchUsers(id, key, pageNum, pageSize);
        return ApiResponse.success(result);
    }


}
