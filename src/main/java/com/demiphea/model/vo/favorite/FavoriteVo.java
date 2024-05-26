package com.demiphea.model.vo.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * FavoriteVo
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteVo {
    private Long id;
    private String name;
    private String description;
    private Integer num;
    @JsonProperty("create_time")
    private LocalDateTime createTime;
    private FavoriteState state;
    private FavoriteConfiguration configuration;
}
