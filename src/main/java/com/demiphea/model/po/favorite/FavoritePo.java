package com.demiphea.model.po.favorite;

import com.demiphea.validation.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FavoritePo
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritePo {
    @NotBlank(message = "收藏夹名称不能为空")
    private String name;
    @NullOrNotBlank(message = "收藏夹简介不能为空")
    private String description;
    private FavoriteConfigurationPo configuration = new FavoriteConfigurationPo();
    private Long favoriteId;
}
