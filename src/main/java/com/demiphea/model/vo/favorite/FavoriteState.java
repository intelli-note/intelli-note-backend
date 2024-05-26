package com.demiphea.model.vo.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FavoriteState
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteState {
    /**
     * 收藏夹是否属于自己
     */
    @JsonProperty("owner_status")
    private Boolean owner;
}
