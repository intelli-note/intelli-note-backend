package com.demiphea.utils.aliyun.ocr;

/**
 * Service
 *
 * @author demiphea
 * @since 17.0.9
 */
public enum ServiceEndpoint {
    OCR(
            "cn-hangzhou",
            "ocr-api.cn-hangzhou.aliyuncs.com"
    ),
    ;
    public final String regionId;
    public final String endpoint;

    ServiceEndpoint(String regionId, String endpoint) {
        this.regionId = regionId;
        this.endpoint = endpoint;
    }
}
