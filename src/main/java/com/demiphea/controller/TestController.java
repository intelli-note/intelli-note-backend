package com.demiphea.controller;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.auth.JwtAuth;
import com.demiphea.model.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author demiphea
 * @since 17.0.9
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/token")
    public ApiResponse token(@RequestParam Long id) {
        String token = JwtAuth.create(id);
        return ApiResponse.success(token);
    }

    @GetMapping("/auth")
    @Auth
    public ApiResponse auth(@AuthID Long id) {
        return ApiResponse.success(id);
    }

    @GetMapping("/unAuth")
    public ApiResponse unAuth() {
        return ApiResponse.success();
    }
}
