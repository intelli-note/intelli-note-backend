package com.demiphea.service.inf.user;

import com.demiphea.model.po.user.WalletUpdate;
import com.demiphea.model.vo.user.Credential;
import com.demiphea.model.vo.user.UserVo;
import com.demiphea.model.vo.user.Wallet;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    Credential licence(@NotNull String code) throws URISyntaxException, IOException, ParseException;

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
    UserVo updateUserProfile(@NotNull Long id, @Nullable String username, @Nullable MultipartFile avatar, @Nullable String biography, @Nullable Integer gender) throws IOException;


    /**
     * 根据ID查询用户
     *
     * @param id       当前用户ID，可以为null
     * @param targetId 查询的用户ID
     * @return {@link UserVo} 用户VO
     * @author demiphea
     */
    UserVo getUserProfile(@Nullable Long id, @NotNull Long targetId);

    /**
     * 获取用户钱包
     *
     * @param id 当前用户ID
     * @return {@link Wallet} 钱包信息
     * @author demiphea
     */
    Wallet getUserWallet(@NotNull Long id);

    /**
     * 更新用户钱包
     *
     * @param id     当前用户ID
     * @param update 更新数据
     * @return {@link Wallet} 钱包信息
     * @author demiphea
     */
    Wallet updateUserWallet(@NotNull Long id, @NotNull WalletUpdate update);
}