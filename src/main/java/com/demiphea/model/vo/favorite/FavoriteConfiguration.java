package com.demiphea.model.vo.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FavoriteConfiguration
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteConfiguration {
    /**
     * 收藏夹是否公开
     */
    @JsonProperty("public_config")
    private Boolean publicConfig;
    /**
     * 是否为默认收藏夹
     */
    @JsonProperty("default_config")
    private Boolean defaultConfig;
}
