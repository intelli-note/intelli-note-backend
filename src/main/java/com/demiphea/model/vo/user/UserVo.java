package com.demiphea.model.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User
 * 用户信息
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    private Long id;
    private String username;
    private String avatar;
    private String biography;
    private String gender;
    @JsonProperty("follows_num")
    private String followsNum;
    @JsonProperty("followers_num")
    private String followersNum;
    private UserState state;
}
