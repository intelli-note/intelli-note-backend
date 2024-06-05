package com.demiphea.controller.edit;

import com.demiphea.auth.Auth;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.service.inf.edit.EditService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * EditController
 *
 * @author demiphea
 * @since 17.0.9
 */
@Validated
@RestController
@RequestMapping("/edit")
@RequiredArgsConstructor
public class EditController {
    private final EditService editService;

    @PostMapping("/link")
    @Auth
    public ApiResponse upload(@RequestParam("file") MultipartFile file) {
        String url = editService.upload(file);
        return ApiResponse.success(url);
    }
}
