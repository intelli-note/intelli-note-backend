package com.demiphea.controller.favorite;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.po.favorite.FavoritePo;
import com.demiphea.model.vo.favorite.FavoriteVo;
import com.demiphea.service.inf.favorite.FavoriteService;
import com.demiphea.validation.group.SimpleOperate;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse insertFavorite(@AuthID Long id, @RequestBody @Validated(SimpleOperate.Insert.class) FavoritePo favoritePo) {
        FavoriteVo favoriteVo = favoriteService.insertFavorite(id, favoritePo);
        return ApiResponse.success(favoriteVo);
    }

    @PutMapping("/{favoriteId}")
    @Auth
    public ApiResponse updateFavorite(@AuthID Long id, @PathVariable Long favoriteId, @RequestBody @Validated(SimpleOperate.Update.class) FavoritePo favoritePo) {
        favoritePo.setFavoriteId(favoriteId);
        FavoriteVo favoriteVo = favoriteService.updateFavorite(id, favoritePo);
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
}
