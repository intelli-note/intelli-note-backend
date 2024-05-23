package com.demiphea.model.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Licence
 * 许可证信息
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Licence {
    /**
     * 接口调用凭证
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 接口调用凭证超时时间，单位（秒）
     */
    @JsonProperty("expires_in")
    private Integer expire;
}
