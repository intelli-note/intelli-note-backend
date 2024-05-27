package com.demiphea.controller.collection;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.vo.collection.CollectionVo;
import com.demiphea.service.inf.collection.CollectionService;
import com.demiphea.validation.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * CollectionController
 *
 * @author demiphea
 * @since 17.0.9
 */
@Validated
@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;

    @PostMapping
    @Auth
    public ApiResponse insertCollection(
            @AuthID
            Long id,
            @RequestParam
            @NotBlank(message = "合集名称不能为空")
            String name,
            @RequestParam(required = false)
            MultipartFile cover,
            @RequestParam(required = false)
            @NullOrNotBlank(message = "合集描述不能为空")
            String description,
            @RequestParam(required = false, defaultValue = "true")
            Boolean publicOption
    ) throws IOException {
        CollectionVo collectionVo = collectionService.insertCollection(id, name, cover, description, publicOption);
        return ApiResponse.success(collectionVo);
    }
}
