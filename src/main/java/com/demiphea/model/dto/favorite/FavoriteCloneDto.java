package com.demiphea.model.dto.favorite;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * FavoriteCloneDto
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@AllArgsConstructor
public class FavoriteCloneDto {
    /**
     * 用户ID
     */
    @NotNull
    private Long userId;
    /**
     * 源收藏夹ID
     */
    @NotNull
    private Long sourceId;
    /**
     * 目标收藏夹ID
     */
    @NotNull
    private Long distId;
}
