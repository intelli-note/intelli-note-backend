package com.demiphea.utils.aliyun.ocr;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.ocr_api20210707.Client;
import com.aliyun.ocr_api20210707.models.RecognizeEduFormulaRequest;
import com.aliyun.ocr_api20210707.models.RecognizeEduFormulaResponse;
import com.aliyun.ocr_api20210707.models.RecognizeEduFormulaResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import com.demiphea.exception.utils.aliyun.ApiResponseCodeStatusException;
import com.demiphea.exception.utils.aliyun.ApiServiceCodeStatusException;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * RecognizeEduFormulaUtil
 * 印刷数学公式识别
 *
 * <p>
 * 详情见 <a href="https://help.aliyun.com/zh/ocr/developer-reference/api-ocr-api-2021-07-07-recognizeeduformula?spm=a2c4g.11186623.0.0.67494c47YNHi2L">接口说明</a>
 * </p>
 *
 * @author demiphea
 * @since 17.0.9
 */
public class RecognizeEduFormulaUtil {
    private static final RuntimeOptions options = new RuntimeOptions();
    private static Client client;

    private static JSONObject parseResult(RecognizeEduFormulaResponse response) {
        Integer statusCode = response.getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new ApiResponseCodeStatusException("调用接口失败，响应码：" + response.getStatusCode());
        }
        RecognizeEduFormulaResponseBody body = response.getBody();
        if (body.getCode() != null && !"200".equals(body.getCode())) {
            throw new ApiServiceCodeStatusException("识别失败(" + body.getCode() + "): " + body.getMessage());
        }
        return JSONObject.parseObject(body.getData());
    }

    /**
     * 接口调用
     *
     * @param url 识别图片URL
     * @return {@link JSONObject} 识别结果
     * @author demiphea
     */
    public static JSONObject call(String url) throws Exception {
        RecognizeEduFormulaRequest request = new RecognizeEduFormulaRequest().setUrl(url);
        RecognizeEduFormulaResponse response = client.recognizeEduFormulaWithOptions(request, options);
        return parseResult(response);
    }

    /**
     * 接口调用
     *
     * @param file 识别文件
     * @return {@link JSONObject} 识别结果
     * @author demiphea
     */
    public static JSONObject call(InputStream file) throws Exception {
        RecognizeEduFormulaRequest request = new RecognizeEduFormulaRequest().setBody(file);
        RecognizeEduFormulaResponse response = client.recognizeEduFormulaWithOptions(request, options);
        return parseResult(response);
    }

    /**
     * 接口调用
     *
     * @param file 识别文件
     * @return {@link JSONObject} 识别结果
     * @author demiphea
     */
    public static JSONObject call(byte[] file) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file);
        return call(byteArrayInputStream);
    }

    /**
     * 接口调用
     *
     * @param file 识别文件
     * @return {@link JSONObject} 识别结果
     * @author demiphea
     */
    public static JSONObject call(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        return call(fileInputStream);
    }

    /**
     * 接口调用
     *
     * @param file 识别文件
     * @return {@link JSONObject} 识别结果
     * @author demiphea
     */
    public static JSONObject call(MultipartFile file) throws Exception {
        return call(file.getInputStream());
    }
}
