package com.demiphea.service.impl.user;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.auth.JwtAuth;
import com.demiphea.common.Constant;
import com.demiphea.dao.UserDao;
import com.demiphea.entity.User;
import com.demiphea.exception.user.Code2SessionException;
import com.demiphea.exception.user.UserDoesNotExistException;
import com.demiphea.model.vo.user.Credential;
import com.demiphea.model.vo.user.Licence;
import com.demiphea.model.vo.user.UserVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.user.UserService;
import com.demiphea.utils.oss.qiniu.OssUtils;
import com.demiphea.utils.wechat.MiniProgramUtils;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

/**
 * UserServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final BaseService baseService;
    private final UserDao userDao;

    @Override
    public Credential licence(@NotNull String code) throws URISyntaxException, IOException, ParseException {
        JSONObject result = MiniProgramUtils.login(code);
        String openId = result.getString("openid");
        if (openId == null) {
            throw new Code2SessionException("用户临时登录凭证有误！");
        }
        User user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openId));
        if (user == null) {
            user = new User(null, "微信用户", Constant.DEFAULT_AVATAR_URL, null, null, openId, BigDecimal.ZERO, BigDecimal.ZERO);
            userDao.insert(user);
        }
        return new Credential(
                baseService.convert(user),
                new Licence(
                        JwtAuth.create(user),
                        JwtAuth.expires().intValue()
                )
        );
    }

    @Override
    public UserVo updateUserProfile(@NotNull Long id, @Nullable String username, @Nullable MultipartFile avatar, @Nullable String biography, @Nullable Integer gender) throws IOException {
        User user = userDao.selectById(id);
        if (username != null) {
            user.setUsername(username);
        }
        if (avatar != null) {
            String avatarUrl = OssUtils.upload(Constant.avatarDir, null, avatar);
            user.setAvatar(avatarUrl);
        }
        if (biography != null) {
            user.setBiography(biography);
        }
        if (gender != null) {
            user.setGender(switch (gender) {
                case 0 -> null;
                case 1 -> true;
                case 2 -> false;
                default -> throw new IllegalStateException("Unexpected value: " + gender);
            });
        }
        userDao.updateById(user);
        return baseService.convert(user);
    }

    @Override
    public UserVo getUserProfile(@Nullable Long id, @NotNull Long targetId) {
        User target = userDao.selectById(targetId);
        if (target == null) {
            throw new UserDoesNotExistException("用户不存在");
        }
        UserVo targetVO = baseService.convert(target);
        if (id != null) {
            baseService.attachState(id, targetVO);
        }
        return targetVO;
    }


}
