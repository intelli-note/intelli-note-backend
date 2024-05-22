package com.demiphea.utils.aliyun.nls.api;

/**
 * PopApiCenter
 * 阿里云RPC POP API接口中心
 *
 * @author demiphea
 * @since 17.0.9
 */
public enum Pop {
    NLS_FILETRANS(
            "nls-filetrans",
            "cn-shanghai",
            "cn-shanghai",
            "filetrans.cn-shanghai.aliyuncs.com"
    ),
    ;
    public final String product;
    public final String regionId;
    public final String endpointName;
    public final String domain;

    Pop(String product, String regionId, String endpointName, String domain) {
        this.product = product;
        this.regionId = regionId;
        this.endpointName = endpointName;
        this.domain = domain;
    }
}
