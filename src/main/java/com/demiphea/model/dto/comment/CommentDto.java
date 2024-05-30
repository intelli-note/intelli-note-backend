package com.demiphea.model.dto.comment;

import com.demiphea.validation.CommentLimit;
import com.demiphea.validation.NullOrNotBlank;
import com.demiphea.validation.NullOrNotEmpty;
import com.demiphea.validation.NullOrURL;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;

/**
 * CommentDto
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@CommentLimit(message = "评论不能为空，并且图片、语音、视频和关联笔记只能选其一")
public class CommentDto {
    @NotNull(message = "需要传入评论所在的笔记ID")
    private Long noteId;
    private Long rootId;
    private Long parentId;

    @NullOrNotBlank(message = "评论文本不为空")
    private String text;
    @NullOrNotEmpty(message = "需要传入图片链接")
    private List<@NotBlank(message = "需要为URL链接") @URL(message = "需要为URL链接") String> imageList;
    @NullOrURL(message = "需要为URL链接")
    private String audio;
    @NullOrURL(message = "需要为URL链接")
    private String video;
    private Long linkNoteId;
}
