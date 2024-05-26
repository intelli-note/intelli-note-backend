package com.demiphea.model.vo.note;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * NoteVo
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteVo {
    private NoteOverviewVo overview;
    private String content;
    private NoteState state;
}
