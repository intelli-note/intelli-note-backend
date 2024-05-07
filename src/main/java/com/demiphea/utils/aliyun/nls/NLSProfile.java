package com.demiphea.utils.aliyun.nls;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * NLSProfile
 * NLS（智能语音交互）配置
 *
 * @author demiphea
 * @since 17.0.9
 */
@Configuration
@ConfigurationProperties("aliyun.nls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NLSProfile {
    private String appKey;
}
