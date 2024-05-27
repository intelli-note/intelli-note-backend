package com.demiphea.service.impl.note;

import com.demiphea.common.Constant;
import com.demiphea.dao.BillDao;
import com.demiphea.dao.NoteDao;
import com.demiphea.dao.UserDao;
import com.demiphea.dao.ViewHistoryDao;
import com.demiphea.entity.Note;
import com.demiphea.entity.User;
import com.demiphea.entity.ViewHistory;
import com.demiphea.exception.common.CommonServiceException;
import com.demiphea.exception.common.ObjectDoesNotExistException;
import com.demiphea.exception.common.PermissionDeniedException;
import com.demiphea.model.bo.user.BillType;
import com.demiphea.model.vo.note.NoteOverviewVo;
import com.demiphea.model.vo.note.NoteVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.SystemService;
import com.demiphea.service.inf.note.NoteService;
import com.demiphea.utils.oss.qiniu.OssUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * NoteServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final BaseService baseService;
    private final SystemService systemService;
    private final NoteDao noteDao;
    private final BillDao billDao;
    private final ViewHistoryDao viewHistoryDao;
    private final UserDao userDao;

    @Override
    public boolean checkAdminPermission(@Nullable Long id, @NotNull Long noteId) {
        if (id == null) {
            return false;
        }
        Note note = noteDao.selectById(noteId);
        if (note == null) {
            throw new ObjectDoesNotExistException("笔记不存在或已删除");
        }
        return checkAdminPermission(id, note);
    }

    @Override
    public boolean checkAdminPermission(@Nullable Long id, @NotNull Note note) {
        if (id == null) {
            return false;
        }
        return id.equals(note.getUserId());
    }

    @Override
    public boolean checkReadPermission(@Nullable Long id, @NotNull Long noteId) {
        Note note = noteDao.selectById(noteId);
        if (note == null) {
            throw new ObjectDoesNotExistException("笔记不存在或已删除");
        }
        return checkReadPermission(id, note);
    }

    @Override
    public boolean checkReadPermission(@Nullable Long id, @NotNull Note note) {
        if (checkAdminPermission(id, note)) {
            return true;
        }
        if (note.getOpenPublic() && (note.getPrice() == null || note.getPrice().compareTo(BigDecimal.ZERO) == 0)) {
            return true;
        }
        if (id == null) {
            return false;
        }
        return billDao.hasBuy(id, note.getId());
    }

    @Override
    public NoteOverviewVo insertNote(@NotNull Long id, @NotNull String title, @Nullable MultipartFile cover, @NotNull String content, @NotNull Boolean openPublic, @NotNull BigDecimal price) throws IOException {
        String coverUrl = null;
        if (cover != null) {
            coverUrl = OssUtils.upload(Constant.NOTE_COVER_DIR, null, cover);
        }
        Note note = new Note(null, title, coverUrl, content, id, LocalDateTime.now(), LocalDateTime.now(), openPublic, price);
        noteDao.insert(note);
        return baseService.convert(id, note);
    }

    @Override
    public NoteOverviewVo updateNote(@NotNull Long id, @NotNull Long noteId, @Nullable String title, @Nullable MultipartFile cover, @Nullable String content, @Nullable Boolean openPublic, @Nullable BigDecimal price) throws IOException {
        Note note = noteDao.selectById(noteId);
        if (note == null) {
            throw new ObjectDoesNotExistException("笔记不存在或已删除");
        }
        if (!checkAdminPermission(id, note)) {
            throw new PermissionDeniedException("权限拒绝访问操作");
        }
        note.setTitle(title);
        if (cover != null) {
            String coverUrl = OssUtils.upload(Constant.NOTE_COVER_DIR, null, cover);
            note.setCover(coverUrl);
        }
        note.setContent(content);
        note.setOpenPublic(openPublic);
        note.setPrice(price);
        note.setUpdateTime(LocalDateTime.now());
        noteDao.updateById(note);
        return baseService.convert(id, noteDao.selectById(noteId));
    }

    @Override
    public void deleteNote(@NotNull Long id, @NotNull Long noteId) {
        if (!checkAdminPermission(id, noteId)) {
            throw new PermissionDeniedException("权限拒绝访问操作");
        }
        noteDao.deleteById(noteId);
    }

    @Override
    public NoteVo readNote(@Nullable Long id, @NotNull Long noteId) {
        Note note = noteDao.selectById(noteId);
        if (note == null) {
            throw new ObjectDoesNotExistException("笔记不存在或已删除");
        }
        if (!checkReadPermission(id, note)) {
            throw new PermissionDeniedException("无法访问私有/未购买的笔记");
        }
        // 记录阅读历史
        if (id != null) {
            CompletableFuture.runAsync(() -> {
                try {
                    viewHistoryDao.insert(new ViewHistory(id, noteId, LocalDateTime.now()));
                } catch (Exception e) {
                    // ignore
                }
            });
        }
        return baseService.pack(id, note);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NoteOverviewVo buyNote(@NotNull Long id, @NotNull Long noteId) {
        if (checkReadPermission(id, noteId) || checkReadPermission(id, noteId)) {
            throw new CommonServiceException("笔记目前无需购买");
        }
        Note note = noteDao.selectById(noteId);
        if (note == null) {
            throw new ObjectDoesNotExistException("笔记不存在或已删除");
        }
        User user = userDao.selectById(id);
        if (user.getBalance().compareTo(note.getPrice()) < 0) {
            throw new CommonServiceException("余额不足，请充值");
        }
        user.setBalance(user.getBalance().subtract(note.getPrice()));
        userDao.updateById(user);
        systemService.insertBill(id, BillType.EXPEND, note.getPrice(), noteId);
        return baseService.convert(id, note);
    }
}
