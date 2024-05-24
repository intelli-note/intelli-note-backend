package com.demiphea.service;

import com.demiphea.model.vo.user.Credential;
import com.demiphea.model.vo.user.UserVo;
import org.apache.hc.core5.http.ParseException;
import org.springframework.web.multipart.MultipartFile;

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
     * @return {@link Credential} 身份凭证信息
     * @author demiphea
     */
    Credential licence(String code) throws URISyntaxException, IOException, ParseException;

    /**
     * 修改用户个人资料
     *
     * @param id        用户ID
     * @param username  用户名
     * @param avatar    头像
     * @param biography 个人简介
     * @param gender    性别
     * @return {@link UserVo} 用户VO
     * @author demiphea
     */
    UserVo updateUserProfile(Long id, String username, MultipartFile avatar, String biography, Integer gender) throws IOException;
}
