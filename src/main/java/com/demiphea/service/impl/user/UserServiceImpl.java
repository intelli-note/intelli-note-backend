package com.demiphea.service.impl.user;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.auth.JwtAuth;
import com.demiphea.common.Constant;
import com.demiphea.dao.UserDao;
import com.demiphea.entity.User;
import com.demiphea.exception.user.BalanceDoesNotEnough;
import com.demiphea.exception.user.Code2SessionException;
import com.demiphea.exception.user.UserDoesNotExistException;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.bo.notice.NoticeType;
import com.demiphea.model.bo.user.BillType;
import com.demiphea.model.dto.user.WalletUpdateDto;
import com.demiphea.model.vo.user.Credential;
import com.demiphea.model.vo.user.Licence;
import com.demiphea.model.vo.user.UserVo;
import com.demiphea.model.vo.user.Wallet;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.ElasticSearchService;
import com.demiphea.service.inf.MessageQueueService;
import com.demiphea.service.inf.SystemService;
import com.demiphea.service.inf.user.UserService;
import com.demiphea.utils.oss.qiniu.OssUtils;
import com.demiphea.utils.wechat.MiniProgramUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

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
    private final SystemService systemService;
    private final MessageQueueService messageQueueService;
    private final UserDao userDao;
    private final ElasticSearchService elasticSearchService;

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
            messageQueueService.saveUserToES(user);
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
            String avatarUrl = OssUtils.upload(Constant.AVATAR_DIR, null, avatar);
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
        messageQueueService.saveUserToES(user);
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

    @Override
    public Wallet getUserWallet(@NotNull Long id) {
        User user = userDao.selectById(id);
        return new Wallet(user.getBalance(), user.getRevenue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Wallet updateUserWallet(@NotNull Long id, @NotNull WalletUpdateDto update) {
        User user = userDao.selectById(id);
        WalletUpdateDto.Operate type = update.getType();
        BigDecimal delta = update.getAmount();
        switch (type) {
            case IN -> {
                user.setBalance(user.getBalance().add(delta));
                Long billId = systemService.insertBill(id, BillType.DEPOSIT, delta, null);
                systemService.publishNotice(id, NoticeType.TRADE, billId);
            }
            case OUT -> {
                if (user.getBalance().compareTo(delta) < 0) {
                    throw new BalanceDoesNotEnough("用户余额不足");
                }
                user.setBalance(user.getBalance().subtract(delta));
                Long billId = systemService.insertBill(id, BillType.WITHDRAW, delta, null);
                systemService.publishNotice(id, NoticeType.TRADE, billId);
            }
        }
        userDao.updateById(user);
        return new Wallet(user.getBalance(), user.getRevenue());
    }

    @Override
    public PageResult searchUsers(@Nullable Long id, @Nullable String key, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        if (key == null || key.isBlank() || key.isEmpty()) {
            Page<Object> page = PageHelper.startPage(pageNum, pageSize);
            List<User> users = userDao.selectList(null);
            PageInfo<User> pageInfo = new PageInfo<>(users);
            List<UserVo> list = users.stream().map(user -> {
                UserVo userVo = baseService.convert(user);
                if (id != null) {
                    baseService.attachState(id, userVo);
                }
                return userVo;
            }).toList();
            PageResult result = new PageResult(pageInfo, list);
            page.close();
            return result;
        }
        List<UserVo> list = elasticSearchService.searchUser(key, pageNum, pageSize).stream()
                .map(userDoc -> {
                    User user = userDao.selectById(userDoc.getId());
                    UserVo userVo = baseService.convert(user);
                    if (id != null) {
                        baseService.attachState(id, userVo);
                    }
                    return userVo;
                }).toList();
        PageInfo<UserVo> pageInfo = new PageInfo<>(list);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        return new PageResult(pageInfo, list);
    }
}
