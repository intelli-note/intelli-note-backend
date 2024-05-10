package com.demiphea.utils.oss.qiniu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * OssProfile
 * 七牛云OSS配置项
 *
 * @author demiphea
 * @since 17.0.9
 */
@Configuration
@ConfigurationProperties("oss")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OssProfile {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String region;
    private String domain;
}
