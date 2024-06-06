package com.demiphea.service.impl.notice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.demiphea.dao.NoticeDao;
import com.demiphea.entity.Notice;
import com.demiphea.exception.common.PermissionDeniedException;
import com.demiphea.model.api.PageResult;
import com.demiphea.model.bo.notice.NoticeType;
import com.demiphea.model.vo.notice.NoticeVo;
import com.demiphea.service.inf.BaseService;
import com.demiphea.service.inf.PermissionService;
import com.demiphea.service.inf.notice.NoticeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * NoticeServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeDao noticeDao;
    private final PermissionService permissionService;
    private final BaseService baseService;

    @Override
    public void putRead(@NotNull Long id, @NotNull Long noticeId) {
        if (!permissionService.checkNoticeAdminPermission(id, noticeId)) {
            throw new PermissionDeniedException("权限拒绝访问操作");
        }
        noticeDao.update(new LambdaUpdateWrapper<Notice>()
                .eq(Notice::getId, noticeId)
                .set(Notice::getReadTick, true)
        );
    }

    @Override
    public void deleteNotice(@NotNull Long id, @NotNull Long noticeId) {
        if (!permissionService.checkNoticeAdminPermission(id, noticeId)) {
            throw new PermissionDeniedException("权限拒绝访问操作");
        }
        noticeDao.deleteById(noticeId);
    }

    @Override
    public PageResult listNotices(@NotNull Long id, @NotNull NoticeType type, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Notice> notices = noticeDao.selectList(new LambdaQueryWrapper<Notice>()
                .eq(Notice::getUserId, id)
                .eq(!type.equals(NoticeType.ALL), Notice::getType, type.type)
                .orderByDesc(Notice::getCreateTime)
        );
        PageInfo<Notice> pageInfo = new PageInfo<>(notices);
        List<NoticeVo> list = notices.stream().map(notice -> baseService.convert(id, notice)).toList();
        PageResult result = new PageResult(pageInfo, list);
        page.close();
        return result;
    }
}
