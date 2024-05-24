package com.demiphea.service.inf;

import com.demiphea.entity.User;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.vo.user.UserVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

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
    @Deprecated
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
     * @param id 当前用户ID
     * @param userVo 用户VO
     * @return {@link UserVo} 用户VO
     * @author demiphea
     */
    UserVo attachState(Long id, UserVo userVo);

    /**
     * 分页结果转换
     *
     * @param pageInfo 分页容器
     * @param list     列表
     * @return {@link PageResult}
     * @author demiphea
     */
    PageResult convert(PageInfo<?> pageInfo, List<?> list);
}
