package com.demiphea.entity.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Objects;

/**
 * UserDoc
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "intelli_note_user", createIndex = false)
public class UserDoc {
    /**
     * ID
     */
    @Id
    private Long id;

    /**
     * 用户ID
     */
    @Field(
            name = "u_id",
            type = FieldType.Keyword,
            index = true,
            copyTo = "summary"
    )
    private Long uId;

    /**
     * 用户名
     */
    @Field(
            name = "username",
            type = FieldType.Text,
            index = true,
            analyzer = "default_analyzer",
            searchAnalyzer = "ik_max_word",
            copyTo = "summary"
    )
    private String username;


    /**
     * 用户简介
     */
    @Field(
            name = "biography",
            type = FieldType.Text,
            index = true,
            analyzer = "default_analyzer",
            searchAnalyzer = "ik_max_word",
            copyTo = "summary"
    )
    private String biography;

    /**
     * 性别
     */
    @Field(
            name = "gender",
            type = FieldType.Keyword,
            index = true
    )
    private String gender;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserDoc obj)) {
            return false;
        }
        return (Objects.equals(this.id, obj.id)) &&
                (Objects.equals(this.uId, obj.uId)) &&
                (Objects.equals(this.username, obj.username)) &&
                (Objects.equals(this.biography, obj.biography)) &&
                (Objects.equals(this.gender, obj.gender));
    }

}
