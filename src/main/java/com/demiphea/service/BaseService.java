package com.demiphea.service;

import com.demiphea.entity.User;
import com.demiphea.model.vo.user.UserVo;

/**
 * BaseService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface BaseService {
    /**
     * 用户转换
     *
     * @param id 用户ID
     * @return {@link UserVo} 用户VO
     * @author demiphea
     */
    UserVo convert(Long id);

    /**
     * 用户转换
     *
     * @param user 用户实体类
     * @return {@link UserVo} 用户VO
     * @author demiphea
     */
    UserVo convert(User user);

    /**
     * 附加状态量
     *
     * @param id     当前用户ID
     * @param userVo 用户VO
     */
    void attachState(Long id, UserVo userVo);
}
