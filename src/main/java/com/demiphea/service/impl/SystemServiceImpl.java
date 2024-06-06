package com.demiphea.service.impl;

import com.demiphea.core.mq.ServiceListener;
import com.demiphea.dao.BillDao;
import com.demiphea.entity.Bill;
import com.demiphea.model.bo.notice.NoticeBo;
import com.demiphea.model.bo.notice.NoticeType;
import com.demiphea.model.bo.user.BillType;
import com.demiphea.service.inf.SystemService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SystemServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class SystemServiceImpl implements SystemService {
    private final BillDao billDao;
    private final RabbitTemplate template;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBill(@NotNull Long userId, @NotNull BillType type, @NotNull BigDecimal amount, @Nullable Long noteId) {
        Bill bill = new Bill(
                null,
                type.ordinal(),
                amount,
                LocalDateTime.now(),
                userId,
                noteId,
                false
        );
        billDao.insert(bill);
    }

    @Override
    public void publishNotice(@NotNull NoticeType type, @NotNull Long linkId, @NotNull Long userId) {
        template.convertAndSend(ServiceListener.EXCHANGE_NAME, "notice.publish", new NoticeBo(type, linkId, userId));
    }
}
