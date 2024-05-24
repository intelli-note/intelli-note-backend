package com.demiphea.service.inf;

import com.demiphea.entity.Bill;
import com.demiphea.entity.Note;
import com.demiphea.entity.User;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.vo.note.NoteOverviewVo;
import com.demiphea.model.vo.user.BillVo;
import com.demiphea.model.vo.user.UserVo;
import com.github.pagehelper.PageInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * @param user 用户实体类
     * @return {@link UserVo} 用户VO
     * @author demiphea
     */
    UserVo convert(@NotNull User user);

    /**
     * 附加状态量
     *
     * @param id 当前用户ID
     * @param userVo 用户VO
     * @return {@link UserVo} 用户VO
     * @author demiphea
     */
    UserVo attachState(@NotNull Long id, @NotNull UserVo userVo);

    /**
     * 笔记转换
     *
     * @param id   当前用户ID
     * @param note 笔记
     * @return {@link NoteOverviewVo} 笔记概览VO
     * @author demiphea
     */
    NoteOverviewVo convert(@Nullable Long id, @NotNull Note note);

    /**
     * 账单转换
     *
     * @param id   当前用户ID
     * @param bill 账单
     * @return {@link BillVo} 账单VO
     * @author demiphea
     */
    BillVo convert(@Nullable Long id, @NotNull Bill bill);

    /**
     * 分页结果转换
     *
     * @param pageInfo 分页容器
     * @param list     列表
     * @return {@link PageResult}
     * @author demiphea
     */
    PageResult convert(@NotNull PageInfo<?> pageInfo, @NotNull List<?> list);
}
