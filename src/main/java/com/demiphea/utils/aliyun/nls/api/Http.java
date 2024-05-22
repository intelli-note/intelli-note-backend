package com.demiphea.utils.aliyun.nls.api;

/**
 * Http
 * 阿里云HTTP API中心
 *
 * @author demiphea
 * @since 17.0.9
 */
public enum Http {
    NLS_SPEECH_FLASH_RECOGNIZER(
            "https://nls-gateway-cn-shanghai.aliyuncs.com/stream/v1/FlashRecognizer",
            "nls-gateway-cn-shanghai.aliyuncs.com"
    ),
    ;
    public final String URL;
    public final String HOST;

    Http(String URL, String HOST) {
        this.URL = URL;
        this.HOST = HOST;
    }
}
