package com.demiphea.utils.aliyun.ai;

import com.demiphea.utils.openai.Model;

/**
 * AliYunModel
 *
 * @author demiphea
 * @since 17.0.9
 */
public enum AliYunModel implements Model {
    QWEN_TRUBO("qwen-turbo"),
    QWEN_PLUS("qwen-plus"),
    QWEN_MAX("qwen-max"),
    QWEN_VL_PLUS("qwen-vl-plus"),
    QWEN_VL_MAX("qwen-vl-max");
    private final String name;

    AliYunModel(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
