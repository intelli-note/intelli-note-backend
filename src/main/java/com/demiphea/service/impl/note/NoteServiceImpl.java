package com.demiphea.service.impl.note;

import com.demiphea.common.Constant;
import com.demiphea.dao.NoteDao;
import com.demiphea.entity.Note;
import com.demiphea.model.vo.note.NoteOverviewVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.note.NoteService;
import com.demiphea.utils.oss.qiniu.OssUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private final NoteDao noteDao;

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
}
