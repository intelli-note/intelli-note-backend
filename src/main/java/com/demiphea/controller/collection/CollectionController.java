package com.demiphea.controller.collection;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.po.note.NoteCollectPo;
import com.demiphea.model.vo.collection.CollectionVo;
import com.demiphea.service.inf.collection.CollectionService;
import com.demiphea.validation.NullOrNotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/{collectionId}")
    @Auth
    public ApiResponse updateCollection(
            @AuthID
            Long id,
            @PathVariable
            Long collectionId,
            @RequestParam(required = false)
            @NullOrNotBlank
            String name,
            @RequestParam(required = false)
            MultipartFile cover,
            @RequestParam(required = false)
            @NullOrNotBlank
            String description,
            @RequestParam(required = false)
            Boolean publicOption
    ) throws IOException {
        CollectionVo collectionVo = collectionService.updateCollection(id, collectionId, name, cover, description, publicOption);
        return ApiResponse.success(collectionVo);
    }

    @DeleteMapping("/{collectionId}")
    @Auth
    public ApiResponse deleteCollection(@AuthID Long id, @PathVariable Long collectionId) {
        collectionService.deleteCollection(id, collectionId);
        return ApiResponse.success();
    }


    @GetMapping
    @Auth(block = false)
    public ApiResponse listCollections(
            @AuthID
            Long id,
            @RequestParam(value = "user_id", required = false)
            Long userId,
            @RequestParam(value = "note_id", required = false)
            Long noteId,
            @RequestParam("page_num")
            @Min(value = 1, message = "页码需要从1开始")
            Integer pageNum,
            @RequestParam("page_size")
            @Min(value = 1, message = "每页数量必须大于1")
            Integer pageSize
    ) {
        PageResult result = collectionService.listCollections(id, userId, noteId, pageNum, pageSize);
        return ApiResponse.success(result);
    }

    @PostMapping("/notes")
    @Auth
    public ApiResponse addNotesToCollections(@AuthID Long id, @RequestBody @Validated NoteCollectPo noteCollectPo) {
        int count = collectionService.addNotesToCollections(id, noteCollectPo.getNoteIds(), noteCollectPo.getCollectionIds());
        return ApiResponse.success(count);
    }
}
