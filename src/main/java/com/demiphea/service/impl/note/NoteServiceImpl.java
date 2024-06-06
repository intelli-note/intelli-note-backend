package com.demiphea.service.impl.note;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demiphea.common.Constant;
import com.demiphea.dao.NoteDao;
import com.demiphea.dao.UserDao;
import com.demiphea.dao.ViewHistoryDao;
import com.demiphea.entity.Note;
import com.demiphea.entity.User;
import com.demiphea.entity.ViewHistory;
import com.demiphea.exception.common.CommonServiceException;
import com.demiphea.exception.common.ObjectDoesNotExistException;
import com.demiphea.exception.common.PermissionDeniedException;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.bo.notice.NoticeType;
import com.demiphea.model.bo.user.BillType;
import com.demiphea.model.dto.note.QueryTypeDto;
import com.demiphea.model.vo.note.NoteOverviewVo;
import com.demiphea.model.vo.note.NoteVo;
import com.demiphea.service.inf.*;
import com.demiphea.service.inf.note.NoteService;
import com.demiphea.utils.oss.qiniu.OssUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private final PermissionService permissionService;
    private final SystemService systemService;
    private final MessageQueueService messageQueueService;
    private final NoteDao noteDao;
    private final ViewHistoryDao viewHistoryDao;
    private final UserDao userDao;
    private final ElasticSearchService elasticSearchService;

    @Override
    public NoteOverviewVo insertNote(@NotNull Long id, @NotNull String title, @Nullable MultipartFile cover, @NotNull String content, @NotNull Boolean openPublic, @NotNull BigDecimal price) throws IOException {
        String coverUrl = null;
        if (cover != null) {
            coverUrl = OssUtils.upload(Constant.NOTE_COVER_DIR, null, cover);
        }
        Note note = new Note(null, title, coverUrl, content, id, LocalDateTime.now(), LocalDateTime.now(), openPublic, price);
        noteDao.insert(note);
        messageQueueService.saveNoteToES(note);
        return baseService.convert(id, note);
    }

    @Override
    public NoteOverviewVo updateNote(@NotNull Long id, @NotNull Long noteId, @Nullable String title, @Nullable MultipartFile cover, @Nullable String content, @Nullable Boolean openPublic, @Nullable BigDecimal price) throws IOException {
        Note note = noteDao.selectById(noteId);
        if (note == null) {
            throw new ObjectDoesNotExistException("笔记不存在或已删除");
        }
        if (!permissionService.checkNoteAdminPermission(id, note)) {
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
        Note newNote = noteDao.selectById(noteId);
        messageQueueService.saveNoteToES(newNote);
        return baseService.convert(id, newNote);
    }

    @Override
    public void deleteNote(@NotNull Long id, @NotNull Long noteId) {
        if (!permissionService.checkNoteAdminPermission(id, noteId)) {
            throw new PermissionDeniedException("权限拒绝访问操作");
        }
        noteDao.deleteById(noteId);
        messageQueueService.deleteNoteInES(noteId);
    }

    @Override
    public NoteVo readNote(@Nullable Long id, @NotNull Long noteId) {
        Note note = noteDao.selectById(noteId);
        if (note == null) {
            throw new ObjectDoesNotExistException("笔记不存在或已删除");
        }
        if (!permissionService.checkNoteReadPermission(id, note)) {
            throw new PermissionDeniedException("无法访问私有/未购买的笔记");
        }
        // 记录阅读历史
        if (id != null) {
            messageQueueService.saveViewHistory(new ViewHistory(id, noteId, LocalDateTime.now()));
        }
        return baseService.pack(id, note);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NoteOverviewVo buyNote(@NotNull Long id, @NotNull Long noteId) {
        if (permissionService.checkNoteAdminPermission(id, noteId) || permissionService.checkNoteReadPermission(id, noteId)) {
            throw new CommonServiceException("笔记目前无需购买");
        }
        Note note = noteDao.selectById(noteId);
        if (note == null) {
            throw new ObjectDoesNotExistException("笔记不存在或已删除");
        }
        // buyer
        User user = userDao.selectById(id);
        if (user.getBalance().compareTo(note.getPrice()) < 0) {
            throw new CommonServiceException("余额不足，请充值");
        }
        user.setBalance(user.getBalance().subtract(note.getPrice()));
        userDao.updateById(user);
        Long buyerBillId = systemService.insertBill(id, BillType.EXPEND, note.getPrice(), noteId);

        // seller
        User author = userDao.selectById(note.getUserId());
        author.setBalance(user.getBalance().add(note.getPrice()));
        author.setRevenue(user.getRevenue().add(note.getPrice()));
        userDao.updateById(author);
        Long sellerBillId = systemService.insertBill(note.getUserId(), BillType.INCOME, note.getPrice(), noteId);

        try {
            systemService.publishNotice(NoticeType.TRADE, buyerBillId);
        } catch (Exception e) {
            // ignore
        }
        try {
            systemService.publishNotice(NoticeType.TRADE, sellerBillId);
        } catch (Exception e) {
            // ignore
        }

        return baseService.convert(id, note);
    }

    @Override
    public PageResult listNotes(@Nullable Long id, @Nullable QueryTypeDto type, @Nullable Long collectionId, @Nullable String key, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        if (type == null) {
            // 查看用户当前笔记
            if (id == null) {
                throw new CommonServiceException("请先登录后再查看");
            }
            Page<Object> page = PageHelper.startPage(pageNum, pageSize);
            List<Note> notes = noteDao.selectList(new LambdaQueryWrapper<Note>()
                    .eq(Note::getUserId, id)
                    .orderByDesc(Note::getCreateTime)
                    .orderByDesc(Note::getUpdateTime)
            );
            PageInfo<Note> pageInfo = new PageInfo<>(notes);
            List<NoteOverviewVo> list = notes.stream().map(note -> baseService.convert(id, note)).toList();
            PageResult result = new PageResult(pageInfo, list);
            page.close();
            return result;
        }
        switch (type) {
            case PURCHASE -> {
                // 查找用户购买笔记
                if (id == null) {
                    throw new CommonServiceException("请先登录后再查看");
                }
                Page<Object> page = PageHelper.startPage(pageNum, pageSize);
                List<Note> notes = noteDao.listUserBuy(id);
                PageInfo<Note> pageInfo = new PageInfo<>(notes);
                List<NoteOverviewVo> list = notes.stream().map(note -> baseService.convert(id, note)).toList();
                PageResult result = new PageResult(pageInfo, list);
                page.close();
                return result;
            }
            case HISTORY -> {
                // 查看用户笔记浏览记录
                if (id == null) {
                    throw new CommonServiceException("请先登录后再查看");
                }
                Page<Object> page = PageHelper.startPage(pageNum, pageSize);
                List<Note> notes = noteDao.listUserView(id);
                PageInfo<Note> pageInfo = new PageInfo<>(notes);
                List<NoteOverviewVo> list = notes.stream().map(note -> baseService.convert(id, note)).toList();
                PageResult result = new PageResult(pageInfo, list);
                page.close();
                return result;
            }
            case COLLECTION -> {
                // 查看某一合集中的笔记
                if (collectionId == null) {
                    throw new CommonServiceException("请传入合集ID参数");
                }
                if (!permissionService.checkCollectionViewPermission(id, collectionId)) {
                    throw new CommonServiceException("当前合集未公开");
                }
                Page<Object> page = PageHelper.startPage(pageNum, pageSize);
                List<Note> notes = noteDao.listCollectionNotes(id, collectionId);
                PageInfo<Note> pageInfo = new PageInfo<>(notes);
                List<NoteOverviewVo> list = notes.stream().map(note -> baseService.convert(id, note)).toList();
                PageResult result = new PageResult(pageInfo, list);
                page.close();
                return result;
            }
            case RECOMMEND -> {
                // 查看推荐的笔记列表
                Page<Object> page = PageHelper.startPage(pageNum, pageSize);
                List<Note> notes = noteDao.selectList(new LambdaQueryWrapper<Note>()
                        .and(w0 -> w0.eq(Note::getOpenPublic, true).or(id != null, w1 -> w1.eq(Note::getUserId, id)))
                        .orderByDesc(Note::getUpdateTime)
                        .orderByDesc(Note::getCreateTime)
                );
                PageInfo<Note> pageInfo = new PageInfo<>(notes);
                List<NoteOverviewVo> list = notes.stream().map(note -> baseService.convert(id, note)).toList();
                PageResult result = new PageResult(pageInfo, list);
                page.close();
                return result;
            }
            case SEARCH -> {
                // 搜索笔记
                if (key == null) {
                    throw new CommonServiceException("如需搜索，请传入关键词");
                }
                List<NoteOverviewVo> list = elasticSearchService.searchNote(key, pageNum, pageSize).stream()
                        .map(noteDoc -> {
                            Note note = noteDao.selectById(noteDoc.getId());
                            return baseService.convert(id, note);
                        }).toList();
                PageInfo<NoteOverviewVo> pageInfo = new PageInfo<>(list);
                pageInfo.setPageNum(pageNum);
                pageInfo.setPageSize(pageSize);
                return new PageResult(pageInfo, list);
            }
        }
        return PageResult.EMPTY;
    }
}
