package com.demiphea.controller;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.vo.note.NoteOverviewVo;
import com.demiphea.service.inf.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * BaseController
 *
 * @author demiphea
 * @since 17.0.9
 */
@Validated
@RestController
@RequiredArgsConstructor
public class BaseController {
    private final NoteService noteService;

    @PostMapping("/purchases")
    @Auth
    public ApiResponse purchases(@AuthID Long id, @RequestParam("note_id") Long noteId) {
        NoteOverviewVo noteOverviewVo = noteService.buyNote(id, noteId);
        return ApiResponse.success(noteOverviewVo);
    }
}
