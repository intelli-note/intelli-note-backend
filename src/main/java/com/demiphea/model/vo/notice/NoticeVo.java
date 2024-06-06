package com.demiphea.model.vo.notice;

import com.demiphea.model.bo.notice.NoticeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * NoticeVo
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeVo {
    /**
     * 通知ID
     */
    private Long id;
    /**
     * 通知类型
     */
    private NoticeType type;
    /**
     * 通知内容
     */
    private INotice notice;
    /**
     * 通知时间
     */
    private LocalDateTime time;
    /**
     * 通知状态-是否已读
     */
    private Boolean read;
}
