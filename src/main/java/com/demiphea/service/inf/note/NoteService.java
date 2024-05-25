package com.demiphea.service.inf.note;

import com.demiphea.entity.Note;
import com.demiphea.model.vo.note.NoteOverviewVo;
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
     * 校验是否具有笔记的管理权限
     *
     * @param id     用户ID
     * @param noteId 笔记ID
     * @return {@link boolean} 是否具有管理权限
     * @author demiphea
     */
    boolean checkAdminPermission(Long id, Long noteId);

    /**
     * 校验是否具有笔记的管理权限
     *
     * @param id   用户ID
     * @param note 笔记
     * @return {@link boolean} 是否具有管理权限
     * @author demiphea
     */
    boolean checkAdminPermission(Long id, Note note);

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
}
