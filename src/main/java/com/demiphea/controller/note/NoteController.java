package com.demiphea.controller.note;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.dto.note.QueryTypeDto;
import com.demiphea.model.vo.note.NoteOverviewVo;
import com.demiphea.model.vo.note.NoteVo;
import com.demiphea.service.inf.note.NoteService;
import com.demiphea.validation.NullOrNotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * NoteController
 *
 * @author demiphea
 * @since 17.0.9
 */
@Validated
@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    @Auth
    public ApiResponse insertNote(
            @AuthID
            Long id,
            @RequestParam
            @NotBlank(message = "标题不能为空")
            String title,
            @RequestParam(required = false)
            MultipartFile cover,
            @RequestParam
            @NotBlank(message = "内容不能为空")
            String content,
            @RequestParam(required = false, defaultValue = "true")
            Boolean publicOption,
            @RequestParam(required = false, defaultValue = "0.00")
            @Digits(integer = 10, fraction = 2, message = "金额格式不正确，需要保留两位小数")
            @DecimalMin(value = "0", message = "金额必须大于或等于0.00")
            BigDecimal freeOption
    ) throws IOException {
        NoteOverviewVo noteOverviewVo = noteService.insertNote(id, title, cover, content, publicOption, freeOption);
        return ApiResponse.success(noteOverviewVo);
    }

    @PostMapping("/{noteId}")
    @Auth
    public ApiResponse updateNote(
            @AuthID
            Long id,
            @PathVariable
            Long noteId,
            @RequestParam(required = false)
            @NullOrNotBlank(message = "标题不能为空")
            String title,
            @RequestParam(required = false)
            MultipartFile cover,
            @RequestParam(required = false)
            @NullOrNotBlank(message = "内容不能为空")
            String content,
            @RequestParam(required = false)
            Boolean publicOption,
            @RequestParam(required = false)
            @Digits(integer = 10, fraction = 2, message = "金额格式不正确，需要保留两位小数")
            @DecimalMin(value = "0", message = "金额必须大于或等于0.00")
            BigDecimal freeOption
    ) throws IOException {
        NoteOverviewVo noteOverviewVo = noteService.updateNote(id, noteId, title, cover, content, publicOption, freeOption);
        return ApiResponse.success(noteOverviewVo);
    }

    @DeleteMapping("/{noteId}")
    @Auth
    public ApiResponse deleteNote(
            @AuthID Long id,
            @PathVariable Long noteId
    ) {
        noteService.deleteNote(id, noteId);
        return ApiResponse.success();
    }

    @GetMapping("/{noteId}")
    @Auth(block = false)
    public ApiResponse readNote(@AuthID Long id, @PathVariable Long noteId) {
        NoteVo note = noteService.readNote(id, noteId);
        return ApiResponse.success(note);
    }


    @GetMapping
    @Auth(block = false)
    public ApiResponse listNotes(
            @AuthID
            @Nullable
            Long id,
            @RequestParam(value = "query_type", required = false)
            String queryType,
            @RequestParam(value = "collection_id", required = false)
            Long collectionId,
            @RequestParam(value = "search_key", required = false)
            @NullOrNotBlank(message = "搜索关键词不能为空")
            String key,
            @RequestParam("page_num")
            @Min(value = 1, message = "页码需要从1开始")
            Integer pageNum,
            @RequestParam("page_size")
            @Min(value = 1, message = "每页数量必须大于1")
            Integer pageSize
    ) {
        QueryTypeDto type = null;
        if (queryType != null) {
            type = QueryTypeDto.valueOf(queryType.toUpperCase());
        }
        PageResult result = noteService.listNotes(id, type, collectionId, key, pageNum, pageSize);
        return ApiResponse.success(result);
    }
}
