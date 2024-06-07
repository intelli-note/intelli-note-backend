package com.demiphea.utils.openai;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Message
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@AllArgsConstructor
public class Message {
    private Role role;
    private String content;

    public enum Role {
        SYSTEM("system"),
        USER("user"),
        ASSISTANT("assistant");
        @JSONField(value = true)
        public final String role;

        Role(String role) {
            this.role = role;
        }
    }
}
