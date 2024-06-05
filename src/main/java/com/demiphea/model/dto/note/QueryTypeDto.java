package com.demiphea.model.dto.note;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * QueryTypeDto
 *
 * @author demiphea
 * @since 17.0.9
 */
public enum QueryTypeDto {
    /**
     * 查询用户购买的笔记
     */
    PURCHASE("purchase"),
    /**
     * 查询用户浏览笔记历史
     */
    HISTORY("history"),
    /**
     * 查询合集中的笔记
     */
    COLLECTION("collection"),
    /**
     * 查询推荐的笔记
     */
    RECOMMEND("recommend"),
    /**
     * 搜索笔记
     */
    SEARCH("search");

    @JsonProperty
    public final String type;

    QueryTypeDto(String type) {
        this.type = type;
    }
}
