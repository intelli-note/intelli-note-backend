package com.demiphea.model.vo.notice;

import com.demiphea.model.vo.collection.CollectionVo;
import com.demiphea.model.vo.note.NoteOverviewVo;
import com.demiphea.model.vo.user.UserVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * StarNotice
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StarNotice implements INotice {
    /**
     * 收藏的用户
     */
    private UserVo user;

    /**
     * 收藏的类型
     */
    private Type type;

    /**
     * 收藏的笔记信息
     */
    private NoteOverviewVo note;

    /**
     * 收藏的收藏夹信息
     */
    private CollectionVo collection;

    /**
     * 收藏时间
     */
    @JsonProperty("star_time")
    private LocalDateTime starTime;

    /**
     * 收藏类型
     */
    public enum Type {
        /**
         * 笔记
         */
        NOTE(0),
        /**
         * 合集
         */
        COLLECTION(1),
        ;

        public final int type;

        Type(int type) {
            this.type = type;
        }
    }
}
