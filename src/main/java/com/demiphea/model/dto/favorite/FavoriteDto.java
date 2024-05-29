package com.demiphea.model.dto.favorite;

import com.demiphea.validation.NullOrNotBlank;
import com.demiphea.validation.group.SimpleOperate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FavoriteDto
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDto {
    @NotBlank(groups = SimpleOperate.Insert.class, message = "收藏夹名称不能为空")
    @NullOrNotBlank(groups = SimpleOperate.Update.class, message = "收藏夹名称不能为空串")
    private String name;
    @NullOrNotBlank(message = "收藏夹简介不能为空")
    private String description;
    private FavoriteConfigurationDto configuration = new FavoriteConfigurationDto();
    @Null(groups = SimpleOperate.Update.class, message = "修改收藏夹时无法拷贝")
    private Long favoriteId;
}
