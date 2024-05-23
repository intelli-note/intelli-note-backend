package com.demiphea.controller;

import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.vo.user.Credential;
import com.demiphea.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
