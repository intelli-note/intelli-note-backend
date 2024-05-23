package com.demiphea.utils.wechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MiniProgramProfile
 * 微信小程序配置项
 *
 * @author demiphea
 * @since 17.0.9
 */
@Configuration
@ConfigurationProperties("mini-program")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniProgramProfile {
    private String appId;
    private String appSecret;
}
