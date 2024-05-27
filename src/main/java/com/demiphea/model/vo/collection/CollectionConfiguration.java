package com.demiphea.model.vo.collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CollectionConfiguration
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionConfiguration {
    @JsonProperty("public_config")
    private Boolean publicConfig;
}
