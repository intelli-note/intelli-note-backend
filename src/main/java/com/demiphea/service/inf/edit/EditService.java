package com.demiphea.service.inf.edit;

import com.alibaba.fastjson2.JSONObject;
import org.apache.hc.core5.http.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * EditService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface EditService {
    /**
     * 获取文件链接
     *
     * @param file 文件
     * @return {@link String} 链接
     * @author demiphea
     */
    String upload(MultipartFile file) throws IOException;

    /**
     * 语音转文字（快速）
     *
     * @param audio 音频文件
     * @param type  类别（需要为{@link com.demiphea.utils.aliyun.nls.SpeechFlashRecognizerUtil.Format}类型）
     * @return {@link JSONObject}  识别结果
     * @author demiphea
     */
    JSONObject speechFlashRecognizer(MultipartFile audio, String type) throws IOException, URISyntaxException, ParseException;

    /**
     * 识别公式
     *
     * @param formula 公式图片
     * @return {@link JSONObject} 识别结果
     * @author demiphea
     */
    JSONObject recognizeFormula(MultipartFile formula) throws Exception;
}
