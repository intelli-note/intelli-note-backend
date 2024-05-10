package com.demiphea.utils.oss.minio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MinioProfile
 * Minio 配置项
 *
 * @author demiphea
 * @since 17.0.9
 */
@Configuration
@ConfigurationProperties("minio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinioProfile {
    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String bucket;
}
