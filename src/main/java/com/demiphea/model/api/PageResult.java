package com.demiphea.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PageResult
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult {
    /**
     * 总页数
     */
    private Integer pages;
    /**
     * 当前页码
     */
    @JsonProperty("page_num")
    private Integer pageNum;
    /**
     * 每页的数量
     */
    @JsonProperty("page_size")
    private Integer pageSize;
    /**
     * 总数
     */
    private Long total;
    /**
     * 当前页的数据列表
     */
    private List<?> list;
    /**
     * 当前页的数据列表长度
     */
    private Integer size;
    /**
     * 是否有前一页
     */
    @JsonProperty("has_previous_page")
    private Boolean hasPreviousPage;
    /**
     * 是否有后一页
     */
    @JsonProperty("has_next_page")
    private Boolean hasNextPage;
    /**
     * 是否为首页
     */
    @JsonProperty("is_first_page")
    private Boolean firstPage;
    /**
     * 是否为尾页
     */
    @JsonProperty("is_last_page")
    private Boolean lastPage;
    /**
     * 首页页码
     */
    @JsonProperty("first_page")
    private Integer firstPageNum;
    /**
     * 尾页页码
     */
    @JsonProperty("last_page")
    private Integer lastPageNum;
}
