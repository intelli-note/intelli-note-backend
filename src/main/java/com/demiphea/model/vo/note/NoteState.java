package com.demiphea.model.vo.note;

import com.demiphea.model.vo.favorite.FavoriteVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * NoteState
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteState {
    @JsonProperty("favorite_list")
    private List<FavoriteVo> favoriteList;
}
