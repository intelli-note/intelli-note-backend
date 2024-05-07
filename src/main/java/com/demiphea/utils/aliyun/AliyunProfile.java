package com.demiphea.utils.aliyun;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AliyunProfile
 * 阿里云配置
 *
 * @author demiphea
 * @since 17.0.9
 */
@Configuration
@ConfigurationProperties("aliyun")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliyunProfile {
    private String accessKey;
    private String secretKey;
}
