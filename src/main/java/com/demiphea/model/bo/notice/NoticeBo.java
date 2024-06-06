package com.demiphea.model.bo.notice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * NoticeBo
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeBo {
    /**
     * 通知类型（不支持{@link NoticeType#ALL}）
     */
    @NotNull
    private NoticeType type;
    /**
     * 关联ID
     */
    @NotNull
    private Long linkId;
    /**
     * 用户ID
     */
    @NotNull
    private Long userId;
}
