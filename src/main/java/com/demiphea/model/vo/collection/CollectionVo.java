package com.demiphea.model.vo.collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * CollectionVo
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionVo {
    private Long id;
    private String name;
    private String cover;
    private String description;
    private Integer num;
    @JsonProperty("star_num")
    private String starNum;
    @JsonProperty("create_time")
    private LocalDateTime createTime;
    private CollectionState state;
    private CollectionConfiguration configuration;
}
