package com.demiphea.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result
 * 响应结果
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    /**
     * 业务码
     */
    private Integer code;
    /**
     * 业务处理信息
     */
    private String msg;
    /**
     * 业务数据
     */
    private Object data;
}
