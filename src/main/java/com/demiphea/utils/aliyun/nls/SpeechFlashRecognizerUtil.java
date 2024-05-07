package com.demiphea.utils.aliyun.nls;

import cn.hutool.core.map.MapBuilder;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nls.client.AccessToken;
import com.demiphea.utils.aliyun.AliyunProfile;
import com.demiphea.utils.aliyun.ApiCenter;
import com.demiphea.utils.network.HttpUtils;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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
@Component
public class SpeechFlashRecognizerUtil {
    private static final String URL = ApiCenter.NLS_SPEECH_FLASH_RECOGNIZER.URL;
    private static final String HOST = ApiCenter.NLS_SPEECH_FLASH_RECOGNIZER.HOST;
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
        String response = HttpUtils.simpleFilePost(URL, parameters, headers, file, ContentType.APPLICATION_OCTET_STREAM);
        return JSON.parseObject(response);
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
        String response = HttpUtils.simpleFilePost(URL, parameters, headers, file, ContentType.APPLICATION_OCTET_STREAM);
        return JSON.parseObject(response);
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
        String response = HttpUtils.simpleFilePost(URL, parameters, headers, file, ContentType.APPLICATION_OCTET_STREAM);
        return JSON.parseObject(response);
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
        String response = HttpUtils.simpleFilePost(URL, parameters, headers, file.getInputStream(), ContentType.APPLICATION_OCTET_STREAM);
        return JSON.parseObject(response);
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
        String response = HttpUtils.simplePost(URL, parameters, headers, null);
        return JSON.parseObject(response);
    }

    @Autowired
    private void initConfig(AliyunProfile aliyunProfile, NLSProfile nlsProfile) {
        SpeechFlashRecognizerUtil.accessToken = new AccessToken(aliyunProfile.getAccessKey(), aliyunProfile.getSecretKey());
        SpeechFlashRecognizerUtil.appKey = nlsProfile.getAppKey();
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
