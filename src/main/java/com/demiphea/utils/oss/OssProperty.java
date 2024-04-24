package com.demiphea.utils.oss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * OssProperty
 *
 * @author demiphea
 * @since 17.0.9
 */
@Configuration
@ConfigurationProperties("oss")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OssProperty {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domain;
}
