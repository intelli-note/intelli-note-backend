package com.demiphea.model.dto.note;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * NoteCollectDto
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteCollectDto {
    @NotEmpty(message = "需要传入要添加的笔记")
    private List<Long> noteIds;
    @NotEmpty(message = "需要传入添加到的合集")
    private List<Long> collectionIds;
}
