package com.demiphea.utils.aliyun.nls;

import cn.hutool.core.map.MapBuilder;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nls.client.AccessToken;
import com.demiphea.exception.utils.aliyun.ApiResponseCodeStatusException;
import com.demiphea.exception.utils.aliyun.ApiServiceCodeStatusException;
import com.demiphea.utils.aliyun.nls.api.Http;
import com.demiphea.utils.network.HttpUtils;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

/**
 * SpeechFlashRecognizerUtil
 * 录音文件识别极速版API
 *
 * <p>
 * 详情见 <a href="https://help.aliyun.com/zh/isi/developer-reference/sdk-reference-9?spm=a2c4g.11186623.0.i8">接口说明</a>
 * </p>
 *
 * @author demiphea
 * @since 17.0.9
 */
public class SpeechFlashRecognizerUtil {
    private static final String URL = Http.NLS_SPEECH_FLASH_RECOGNIZER.URL;
    private static final String HOST = Http.NLS_SPEECH_FLASH_RECOGNIZER.HOST;
    private static AccessToken accessToken;
    private static String appKey;

    private static String getToken() throws IOException {
        long expireTime = accessToken.getExpireTime();
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        if (expireTime - now < 12 * 60 * 60) {
            accessToken.apply();
        }
        return accessToken.getToken();
    }

    private static Map<String, String> parameters(Format format) throws IOException {
        return MapBuilder.<String, String>create()
                .put("appkey", appKey)
                .put("format", format.name())
                .put("token", getToken())
                .build();
    }

    private static Map<String, Object> headers() {
        return MapBuilder.<String, Object>create()
                .put("Content-type", "application/octet-stream")
                .put("Host", HOST)
                .build();
    }

    private static JSONObject parseResponse(ClassicHttpResponse response) throws IOException, ParseException {
        if (HttpStatus.SC_OK != response.getCode()) {
            throw new ApiResponseCodeStatusException("调用接口失败，响应码：" + response.getCode());
        }
        String res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();

        JSONObject result = JSONObject.parseObject(res);
        int statusCode = result.getIntValue("status");
        if (Status.SUCCESS.code != statusCode) {
            throw new ApiServiceCodeStatusException("识别失败(" + statusCode + "): " + result.getString("message"));
        }
        return result.getJSONObject("flash_result");
    }

    /**
     * 接口调用
     *
     * @param file   文件
     * @param format 音频编码格式
     * @return {@link JSONObject} 返回内容
     * @author demiphea
     */
    public static JSONObject call(File file, Format format) throws IOException, URISyntaxException, ParseException {
        Map<String, String> parameters = parameters(format);
        Map<String, Object> headers = headers();
        ClassicHttpResponse response = HttpUtils.filePost(URL, parameters, headers, file, ContentType.APPLICATION_OCTET_STREAM);
        return parseResponse(response);
    }

    /**
     * 接口调用
     *
     * @param file   文件
     * @param format 音频编码格式
     * @return {@link JSONObject} 返回内容
     * @author demiphea
     */
    public static JSONObject call(byte[] file, Format format) throws IOException, URISyntaxException, ParseException {
        Map<String, String> parameters = parameters(format);
        Map<String, Object> headers = headers();
        ClassicHttpResponse response = HttpUtils.filePost(URL, parameters, headers, file, ContentType.APPLICATION_OCTET_STREAM);
        return parseResponse(response);
    }

    /**
     * 接口调用
     *
     * @param file   文件
     * @param format 音频编码格式
     * @return {@link JSONObject} 返回内容
     * @author demiphea
     */
    public static JSONObject call(InputStream file, Format format) throws IOException, URISyntaxException, ParseException {
        Map<String, String> parameters = parameters(format);
        Map<String, Object> headers = headers();
        ClassicHttpResponse response = HttpUtils.filePost(URL, parameters, headers, file, ContentType.APPLICATION_OCTET_STREAM);
        return parseResponse(response);
    }

    /**
     * 接口调用
     *
     * @param file   文件
     * @param format 音频编码格式
     * @return {@link JSONObject} 返回内容
     * @author demiphea
     */
    public static JSONObject call(MultipartFile file, Format format) throws IOException, URISyntaxException, ParseException {
        Map<String, String> parameters = parameters(format);
        Map<String, Object> headers = headers();
        ClassicHttpResponse response = HttpUtils.filePost(URL, parameters, headers, file.getInputStream(), ContentType.APPLICATION_OCTET_STREAM);
        return parseResponse(response);
    }

    /**
     * 接口调用
     *
     * @param fileUrl 文件访问链接
     * @param format  音频编码格式
     * @return {@link JSONObject} 返回内容
     * @author demiphea
     */
    public static JSONObject call(String fileUrl, Format format) throws IOException, URISyntaxException, ParseException {
        Map<String, String> parameters = MapBuilder.<String, String>create()
                .put("appkey", appKey)
                .put("format", format.name())
                .put("token", getToken())
                .put("audio_address", fileUrl)
                .build();
        Map<String, Object> headers = MapBuilder.<String, Object>create()
                .put("Content-type", "application/text")
                .put("Host", HOST)
                .build();
        ClassicHttpResponse response = HttpUtils.post(URL, parameters, headers, null);
        return parseResponse(response);
    }

    /**
     * 响应业务状态
     *
     * @author demiphea
     * @since 17.0.9
     */
    private enum Status {
        SUCCESS(20000000),
        ;
        public final int code;

        Status(int code) {
            this.code = code;
        }
    }

    /**
     * 音频编码格式枚举类
     */
    public enum Format {
        MP4,
        AAC,
        MP3,
        OPUS,
        WAV;
    }
}
