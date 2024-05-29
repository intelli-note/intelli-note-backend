package com.demiphea.model.dto.favorite;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * FavoriteManagementDto
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteManagementDto {
    private List<Long> noteIds;
    private List<Long> collectionIds;
    @NotEmpty(message = "收藏夹列表不能为空")
    private List<Long> favoriteIds;
}
