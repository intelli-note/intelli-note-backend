package com.demiphea.entity.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NoteDoc
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "intelli_note_note", createIndex = false)
public class NoteDoc {
    /**
     * ID
     */
    @Id
    private Long id;

    /**
     * 笔记ID
     */
    @Field(
            name = "title",
            type = FieldType.Text,
            index = true,
            analyzer = "default_analyzer",
            searchAnalyzer = "ik_max_word",
            copyTo = "summary"
    )
    private String title;

    /**
     * 内容
     */
    @Field(
            name = "content",
            type = FieldType.Text,
            index = true,
            analyzer = "default_analyzer",
            searchAnalyzer = "ik_max_word",
            copyTo = "summary"
    )
    private String content;

    /**
     * 作者ID
     */
    @Field(
            name = "u_id",
            type = FieldType.Keyword,
            index = true,
            copyTo = "summary"
    )
    private Long userId;

    /**
     * 作者名称
     */
    @Field(
            name = "username",
            index = true,
            analyzer = "default_analyzer",
            searchAnalyzer = "ik_max_word",
            copyTo = "summary"
    )
    private String username;

    /**
     * 创建时间
     */
    @Field(
            name = "create_time",
            type = FieldType.Date,
            index = true,
            format = {}, // custom
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Field(
            name = "update_time",
            type = FieldType.Date,
            index = true,
            format = {}, // custom
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 价格
     */
    @Field(
            name = "price",
            type = FieldType.Double,
            index = true
    )
    private BigDecimal price;
}
