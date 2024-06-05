package com.demiphea.service.inf.note;

import com.demiphea.model.api.PageResult;
import com.demiphea.model.dto.note.QueryTypeDto;
import com.demiphea.model.vo.note.NoteOverviewVo;
import com.demiphea.model.vo.note.NoteVo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * NoteService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface NoteService {
    /**
     * 新增笔记
     *
     * @param id         当前用户ID
     * @param title      笔记标题
     * @param cover      笔记封面
     * @param content    笔记内容
     * @param openPublic 开放权限，true-公开，false-私有
     * @param price      价格
     * @return {@link NoteOverviewVo} 笔记概览VO
     * @author demiphea
     */
    NoteOverviewVo insertNote(@NotNull Long id, @NotNull String title, @Nullable MultipartFile cover, @NotNull String content, @NotNull Boolean openPublic, @NotNull BigDecimal price) throws IOException;

    /**
     * 修改笔记
     *
     * @param id         当前用户ID
     * @param noteId     笔记ID
     * @param title      笔记标题
     * @param cover      笔记封面
     * @param content    笔记内容
     * @param openPublic 开放权限，true-公开，false-私有
     * @param price      价格
     * @return {@link NoteOverviewVo} 笔记概览VO
     * @author demiphea
     */
    NoteOverviewVo updateNote(@NotNull Long id, @NotNull Long noteId, @Nullable String title, @Nullable MultipartFile cover, @Nullable String content, @Nullable Boolean openPublic, @Nullable BigDecimal price) throws IOException;

    /**
     * 删除笔记
     *
     * @param id     当前用户ID
     * @param noteId 笔记ID
     * @author demiphea
     */
    void deleteNote(@NotNull Long id, @NotNull Long noteId);

    /**
     * 查阅某一个笔记
     *
     * @param id     当前用户ID
     * @param noteId 笔记ID
     * @return {@link NoteVo} 笔记VO
     * @author demiphea
     */
    NoteVo readNote(@Nullable Long id, @NotNull Long noteId);

    /**
     * 购买笔记
     *
     * @param id     当前用户ID
     * @param noteId 笔记ID
     * @return {@link NoteOverviewVo} 笔记概览VO
     * @author demiphea
     */
    NoteOverviewVo buyNote(@NotNull Long id, @NotNull Long noteId);

    /**
     * 分页获取笔记列表
     *
     * @param id           当前用户ID
     * @param type         查询类型
     * @param collectionId 合集ID
     * @param key          搜索关键词
     * @param pageNum      页码
     * @param pageSize     每页数量
     * @return {@link PageResult} 结果
     * @author demiphea
     */
    PageResult listNotes(@Nullable Long id, @Nullable QueryTypeDto type, @Nullable Long collectionId, @Nullable String key, @NotNull Integer pageNum, @NotNull Integer pageSize);
}
