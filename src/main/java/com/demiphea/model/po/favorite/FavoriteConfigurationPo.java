package com.demiphea.model.po.favorite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FavoriteConfigurationPo
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteConfigurationPo {
    private boolean publicConfig = true;
    private boolean defaultConfig = false;
}
