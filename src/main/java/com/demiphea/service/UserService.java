package com.demiphea.service;

import com.demiphea.entity.User;
import com.demiphea.model.vo.user.Credential;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * UserService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface UserService {
    /**
     * 获取用户身份凭证
     *
     * @param code 登录凭证
     * @return {@link User} 用户信息
     * @author demiphea
     */
    Credential licence(String code) throws URISyntaxException, IOException, ParseException;

}
