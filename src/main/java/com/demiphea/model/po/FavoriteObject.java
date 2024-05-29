package com.demiphea.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * FavoriteObject
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteObject {
    private Long id;
    private Boolean type;
    private LocalDateTime createTime;
}
