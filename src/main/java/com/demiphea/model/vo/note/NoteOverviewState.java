package com.demiphea.model.vo.note;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * NoteOverviewState
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteOverviewState {
    @JsonProperty("star_status")
    private Boolean star;
    @JsonProperty("owner_status")
    private Boolean owner;
}
