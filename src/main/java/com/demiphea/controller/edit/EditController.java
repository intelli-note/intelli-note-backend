package com.demiphea.controller.edit;

import com.alibaba.fastjson2.JSONObject;
import com.demiphea.auth.Auth;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.service.inf.edit.EditService;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

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
    public ApiResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
        String url = editService.upload(file);
        return ApiResponse.success(url);
    }

    @PostMapping("/nls/speech-flash-recognizer")
    @Auth
    public ApiResponse speechFlashRecognizer(@RequestParam("audio") MultipartFile audio, @RequestParam("type") String type) throws IOException, URISyntaxException, ParseException {
        JSONObject result = editService.speechFlashRecognizer(audio, type);
        return ApiResponse.success(result);
    }

    @PostMapping("/ocr/formula")
    @Auth
    public ApiResponse recognizeFormula(@RequestParam("formula") MultipartFile formula) throws Exception {
        JSONObject result = editService.recognizeFormula(formula);
        return ApiResponse.success(result);
    }
}
