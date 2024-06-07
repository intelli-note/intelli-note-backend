package com.demiphea.utils.aliyun.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AIProfile
 * AI配置
 *
 * @author demiphea
 * @since 17.0.9
 */
@Configuration
@ConfigurationProperties("aliyun.ai")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIProfile {
    private String apikey;
}
