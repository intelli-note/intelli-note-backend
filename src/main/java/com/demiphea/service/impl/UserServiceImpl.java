package com.demiphea.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.auth.JwtAuth;
import com.demiphea.common.Constant;
import com.demiphea.dao.UserDao;
import com.demiphea.entity.User;
import com.demiphea.exception.user.Code2SessionException;
import com.demiphea.model.vo.user.Credential;
import com.demiphea.model.vo.user.Licence;
import com.demiphea.service.BaseService;
import com.demiphea.service.UserService;
import com.demiphea.utils.wechat.MiniProgramUtils;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

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
    public Credential licence(String code) throws URISyntaxException, IOException, ParseException {
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
}
