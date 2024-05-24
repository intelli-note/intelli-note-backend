package com.demiphea.model.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserState
 * 用户状态量
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserState {
    @JsonProperty("follow_status")
    private FollowStatus followStatus;

    @JsonProperty("self_status")
    private Boolean selfStatus;

    public enum FollowStatus {
        UNFOLLOWED(0),
        FOLLOWED(1),
        FOLLOW_EACH_OTHER(2);

        @JsonValue
        public final int status;

        FollowStatus(int status) {
            this.status = status;
        }
    }
}
