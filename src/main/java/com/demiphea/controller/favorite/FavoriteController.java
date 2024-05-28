package com.demiphea.controller.favorite;

import com.demiphea.auth.Auth;
import com.demiphea.auth.AuthID;
import com.demiphea.model.api.ApiResponse;
import com.demiphea.model.po.favorite.FavoritePo;
import com.demiphea.model.vo.favorite.FavoriteVo;
import com.demiphea.service.inf.favorite.FavoriteService;
import com.demiphea.validation.group.SimpleOperate;
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
}
