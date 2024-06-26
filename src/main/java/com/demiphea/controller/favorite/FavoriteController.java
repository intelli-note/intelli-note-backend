package com.demiphea.controller.favorite;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.dto.favorite.FavoriteDto;
import com.demiphea.model.dto.favorite.FavoriteManagementDto;
import com.demiphea.model.vo.favorite.FavoriteVo;
import com.demiphea.service.inf.favorite.FavoriteService;
import com.demiphea.validation.group.SimpleOperate;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FavoriteController
 *
 * @author demiphea
 * @since 17.0.9
 */
@Validated
@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    @Auth
    public ApiResponse insertFavorite(@AuthID Long id, @RequestBody @Validated(SimpleOperate.Insert.class) FavoriteDto favoriteDto) {
        FavoriteVo favoriteVo = favoriteService.insertFavorite(id, favoriteDto);
        return ApiResponse.success(favoriteVo);
    }

    @PutMapping("/{favoriteId}")
    @Auth
    public ApiResponse updateFavorite(@AuthID Long id, @PathVariable Long favoriteId, @RequestBody @Validated(SimpleOperate.Update.class) FavoriteDto favoriteDto) {
        favoriteDto.setFavoriteId(favoriteId);
        FavoriteVo favoriteVo = favoriteService.updateFavorite(id, favoriteDto);
        return ApiResponse.success(favoriteVo);
    }

    @DeleteMapping("/{favoriteId}")
    @Auth
    public ApiResponse deleteFavorite(@AuthID Long id, @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(id, favoriteId);
        return ApiResponse.success();
    }

    @GetMapping
    @Auth(block = false)
    public ApiResponse listFavorites(
            @AuthID
            Long id,
            @RequestParam(value = "user_id", required = false)
            Long userId,
            @RequestParam("page_num")
            @Min(value = 1, message = "页码需要从1开始")
            Integer pageNum,
            @RequestParam("page_size")
            @Min(value = 1, message = "每页数量必须大于1")
            Integer pageSize
    ) {
        PageResult result = favoriteService.listFavorites(id, userId, pageNum, pageSize);
        return ApiResponse.success(result);
    }

    @PostMapping("/management")
    @Auth
    public ApiResponse insertNoteOrCollectionToFavorite(
            @AuthID
            Long id,
            @RequestBody
            @Validated
            FavoriteManagementDto favoriteManagementDto
    ) {
        int count = favoriteService.insertNoteOrCollectionToFavorite(id, favoriteManagementDto.getNoteIds(), favoriteManagementDto.getCollectionIds(), favoriteManagementDto.getFavoriteIds());
        return ApiResponse.success(count);
    }

    @DeleteMapping("/management/{favoriteId}")
    @Auth
    public ApiResponse deleteNoteOrCollectionInFavorite(
            @AuthID
            Long id,
            @PathVariable
            Long favoriteId,
            @RequestParam(value = "note_id", required = false)
            List<Long> noteIds,
            @RequestParam(value = "collection_id", required = false)
            List<Long> collectionIds
    ) {
        int count = favoriteService.deleteNoteOrCollectionInFavorite(id, favoriteId, noteIds, collectionIds);
        return ApiResponse.success(count);
    }

    @GetMapping("/management")
    @Auth(block = false)
    public ApiResponse listFavoriteContent(
            @AuthID
            Long id,
            @RequestParam("favorite_id")
            Long favoriteId,
            @RequestParam("page_num")
            @Min(value = 1, message = "页码需要从1开始")
            Integer pageNum,
            @RequestParam("page_size")
            @Min(value = 1, message = "每页数量必须大于1")
            Integer pageSize
    ) {
        PageResult result = favoriteService.listFavoriteContent(id, favoriteId, pageNum, pageSize);
        return ApiResponse.success(result);
    }
}
