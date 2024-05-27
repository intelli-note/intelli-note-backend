package com.demiphea.model.vo.collection;

import com.demiphea.model.vo.favorite.FavoriteVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * CollectionState
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionState {
    /**
     * 当前用户是否收藏该合集
     */
    @JsonProperty("star_status")
    Boolean star;
    /**
     * 当前用户是否拥有该合集
     */
    @JsonProperty("owner_status")
    Boolean owner;
    /**
     * 当前用户收藏该合集的收藏夹列表
     */
    @JsonProperty("favorite_list")
    List<FavoriteVo> favoriteList;
}
