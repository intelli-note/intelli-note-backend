package com.demiphea.model.vo.comment;

import com.demiphea.model.vo.note.NoteOverviewVo;
import com.demiphea.model.vo.user.UserVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CommentVo
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {
    private Long id;
    private UserVo user;

    @JsonProperty("reply_comment")
    private Replied replied;

    private String text;
    @JsonProperty("image_list")
    private List<String> imageList;
    private String audio;
    private String video;
    private NoteOverviewVo note;

    @JsonProperty("agree_num")
    private String agreeNum;
    @JsonIgnore
    private Long agreeNumber;
    @JsonProperty("reply_num")
    private String replyNum;
    @JsonIgnore
    private Long replyNumber;
    @JsonProperty("create_time")
    private LocalDateTime createTime;

    private CommentState state;
}
