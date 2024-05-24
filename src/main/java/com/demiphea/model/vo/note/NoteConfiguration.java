package com.demiphea.model.vo.note;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * NoteConfiguration
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteConfiguration {
    /**
     * 是否公开
     */
    @JsonProperty("public_config")
    private Boolean openPublic;

    /**
     * 价格，免费为null/0
     */
    @JsonProperty("free_config")
    private BigDecimal price;
}
