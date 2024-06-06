package com.demiphea.model.vo.notice;

import com.demiphea.model.vo.user.UserVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * FollowNotice
 * 关注通知
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowNotice implements INotice {
    /**
     * 粉丝信息
     */
    private UserVo follower;
    /**
     * 被关注时间
     */
    @JsonProperty("follow_time")
    private LocalDateTime followTime;
}
