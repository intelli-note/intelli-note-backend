package com.demiphea.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Credential
 * 用户身份凭证
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credential {
    /**
     * 用户信息
     */
    private UserVo user;
    /**
     * 许可证信息
     */
    private Licence licence;
}
