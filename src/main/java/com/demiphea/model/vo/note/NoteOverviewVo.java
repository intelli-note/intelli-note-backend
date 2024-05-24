package com.demiphea.model.vo.note;

import com.demiphea.model.vo.user.UserVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * NoteOverviewVo
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteOverviewVo {
    private Long id;
    private UserVo author;
    private String title;
    private String cover;
    @JsonProperty("view_num")
    private String viewNum;
    @JsonProperty("star_num")
    private String starNum;
    @JsonProperty("comment_num")
    private String commentNum;
    @JsonProperty("create_time")
    private LocalDateTime createTime;
    @JsonProperty("update_time")
    private LocalDateTime updateTime;
    private NoteOverviewState state;
    private NoteConfiguration configuration;
}
