package com.demiphea.model.po.favorite;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * FavoriteManagementPo
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteManagementPo {
    private List<Long> noteIds;
    private List<Long> collectionIds;
    @NotEmpty(message = "收藏夹列表不能为空")
    private List<Long> favoriteIds;
}
