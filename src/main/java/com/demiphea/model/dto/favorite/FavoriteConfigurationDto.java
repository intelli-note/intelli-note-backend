package com.demiphea.model.dto.favorite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FavoriteConfigurationDto
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteConfigurationDto {
    private boolean publicConfig = true;
    private boolean defaultConfig = false;
}
