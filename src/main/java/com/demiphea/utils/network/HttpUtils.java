package com.demiphea.utils.network;

import com.alibaba.fastjson2.JSON;
import com.demiphea.exception.utils.network.FormTypeException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.*;
import org.apache.hc.core5.net.URIBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HttpUtils
 * 网络请求工具类
 *
 * @author demiphea
 * @since 17.0.9
 */
public class HttpUtils {
    private static CloseableHttpClient client;

    private static URI buildURI(String url, Map<String, String> parameters) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (parameters != null) {
            parameters.forEach(uriBuilder::addParameter);
        }
        return uriBuilder.build();
    }

    /**
     * 发起 GET 请求
     *
     * @param url        链接
     * @param parameters 请求参数
     * @param headers    请求头
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse get(String url, Map<String, String> parameters, Map<String, Object> headers) throws URISyntaxException, IOException {
        HttpGet httpGet = new HttpGet(buildURI(url, parameters));
        if (headers != null) {
            headers.forEach(httpGet::addHeader);
        }
        return client.executeOpen(null, httpGet, null);
    }

    /**
     * 发起 GET 请求
     *
     * @param url        链接
     * @param parameters 请求参数
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse get(String url, Map<String, String> parameters) throws URISyntaxException, IOException {
        return get(url, parameters, null);
    }

    /**
     * 发起 GET 请求
     *
     * @param url 链接
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse get(String url) throws URISyntaxException, IOException {
        return get(url, null, null);
    }

    /**
     * 发起 GET 请求
     *
     * @param url        链接
     * @param parameters 请求参数
     * @param headers    请求头
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleGet(String url, Map<String, String> parameters, Map<String, Object> headers) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = get(url, parameters, headers);
        String str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();
        return str;
    }

    /**
     * 发起 GET 请求
     *
     * @param url        链接
     * @param parameters 请求参数
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleGet(String url, Map<String, String> parameters) throws URISyntaxException, IOException, ParseException {
        return simpleGet(url, parameters, null);
    }

    /**
     * 发起 GET 请求
     *
     * @param url 链接
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleGet(String url) throws URISyntaxException, IOException, ParseException {
        return simpleGet(url, null, null);
    }


    /**
     * 发起 POST 请求
     *
     * @param url        链接
     * @param parameters 请求参数
     * @param headers    请求头
     * @param body       请求体（会被JSON序列化）
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse post(String url, Map<String, String> parameters, Map<String, Object> headers, Object body) throws URISyntaxException, IOException {
        HttpPost httpPost = new HttpPost(buildURI(url, parameters));
        if (headers != null) {
            headers.forEach(httpPost::addHeader);
        }
        if (body != null) {
            httpPost.setEntity(new StringEntity(JSON.toJSONString(body)));
        }
        return client.executeOpen(null, httpPost, null);
    }

    /**
     * 发起 POST 请求
     *
     * @param url     链接
     * @param headers 请求头
     * @param body    请求体（会被JSON序列化）
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse post(String url, Map<String, Object> headers, Object body) throws URISyntaxException, IOException {
        return post(url, null, headers, body);
    }

    /**
     * 发起 POST 请求
     *
     * @param url  链接
     * @param body 请求体（会被JSON序列化）
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse post(String url, Object body) throws URISyntaxException, IOException {
        return post(url, null, null, body);
    }

    /**
     * 发起 POST 请求
     *
     * @param url 链接
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse post(String url) throws URISyntaxException, IOException {
        return post(url, null, null, null);
    }

    /**
     * 发起 POST 请求
     *
     * @param url        链接
     * @param parameters 请求参数
     * @param headers    请求头
     * @param body       请求体（会被JSON序列化）
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simplePost(String url, Map<String, String> parameters, Map<String, Object> headers, Object body) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = post(url, parameters, headers, body);
        String str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();
        return str;
    }

    /**
     * 发起 POST 请求
     *
     * @param url     链接
     * @param headers 请求头
     * @param body    请求体（会被JSON序列化）
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simplePost(String url, Map<String, Object> headers, Object body) throws URISyntaxException, IOException, ParseException {
        return simplePost(url, null, headers, body);
    }

    /**
     * 发起 POST 请求
     *
     * @param url  链接
     * @param body 请求体（会被JSON序列化）
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simplePost(String url, Object body) throws URISyntaxException, IOException, ParseException {
        return simplePost(url, null, null, body);
    }

    /**
     * 发起 POST 请求
     *
     * @param url 链接
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simplePost(String url) throws URISyntaxException, IOException, ParseException {
        return simplePost(url, null, null, null);
    }

    /**
     * 发起 POST 请求（表单）
     *
     * @param url        链接
     * @param parameters 请求参数
     * @param headers    请求头
     * @param formData   表单数据
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse formPost(String url, Map<String, String> parameters, Map<String, Object> headers, Map<String, Object> formData) throws URISyntaxException, IOException {
        HttpPost httpPost = new HttpPost(buildURI(url, parameters));
        if (headers != null) {
            headers.forEach(httpPost::addHeader);
        }
        if (formData != null) {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            formData.forEach((k, v) -> {
                if (v instanceof File file) {
                    entityBuilder.addPart(k, new FileBody(file));
                } else if (v instanceof String value) {
                    entityBuilder.addPart(k, new StringBody(value, ContentType.MULTIPART_FORM_DATA));
                } else if (v instanceof byte[] file) {
                    entityBuilder.addPart(k, new ByteArrayBody(file, k));
                } else if (v instanceof InputStream file) {
                    entityBuilder.addPart(k, new InputStreamBody(file, k));
                } else {
                    throw new FormTypeException("未知表单项");
                }
            });
            httpPost.setEntity(entityBuilder.build());
        }
        return client.executeOpen(null, httpPost, null);
    }

    /**
     * 发起 POST 请求（表单）
     *
     * @param url      链接
     * @param headers  请求头
     * @param formData 表单数据
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse formPost(String url, Map<String, Object> headers, Map<String, Object> formData) throws URISyntaxException, IOException {
        return formPost(url, null, headers, formData);
    }

    /**
     * 发起 POST 请求（表单）
     *
     * @param url      链接
     * @param formData 表单数据
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse formPost(String url, Map<String, Object> formData) throws URISyntaxException, IOException {
        return formPost(url, null, null, formData);
    }

    /**
     * 发起 POST 请求（表单）
     *
     * @param url 链接
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse formPost(String url) throws URISyntaxException, IOException {
        return formPost(url, null, null, null);
    }

    /**
     * 发起 POST 请求（表单）
     *
     * @param url        链接
     * @param parameters 请求参数
     * @param headers    请求头
     * @param formData   表单数据
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFormPost(String url, Map<String, String> parameters, Map<String, Object> headers, Map<String, Object> formData) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = formPost(url, parameters, headers, formData);
        String str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();
        return str;
    }

    /**
     * 发起 POST 请求（表单）
     *
     * @param url      链接
     * @param headers  请求头
     * @param formData 表单数据
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFormPost(String url, Map<String, Object> headers, Map<String, Object> formData) throws URISyntaxException, IOException, ParseException {
        return simpleFormPost(url, null, headers, formData);
    }

    /**
     * 发起 POST 请求（表单）
     *
     * @param url      链接
     * @param formData 表单数据
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFormPost(String url, Map<String, Object> formData) throws URISyntaxException, IOException, ParseException {
        return simpleFormPost(url, null, null, formData);
    }

    /**
     * 发起 POST 请求（表单）
     *
     * @param url 链接
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFormPost(String url) throws URISyntaxException, IOException, ParseException {
        return simpleFormPost(url, null, null, null);
    }

    /**
     * 文件上传（POST）
     *
     * @param url         链接
     * @param parameters  请求参数
     * @param headers     请求头
     * @param file        文件
     * @param contentType 类型
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse filePost(String url, Map<String, String> parameters, Map<String, Object> headers, File file, ContentType contentType) throws URISyntaxException, IOException {
        HttpPost httpPost = new HttpPost(buildURI(url, parameters));
        if (headers != null) {
            headers.forEach(httpPost::addHeader);
        }
        FileEntity entity = new FileEntity(file, contentType);
        httpPost.setEntity(entity);
        return client.executeOpen(null, httpPost, null);
    }

    /**
     * 文件上传（POST）
     *
     * @param url         链接
     * @param parameters  请求参数
     * @param headers     请求头
     * @param file        文件
     * @param contentType 类型
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse filePost(String url, Map<String, String> parameters, Map<String, Object> headers, byte[] file, ContentType contentType) throws URISyntaxException, IOException {
        HttpPost httpPost = new HttpPost(buildURI(url, parameters));
        if (headers != null) {
            headers.forEach(httpPost::addHeader);
        }
        ByteArrayEntity entity = new ByteArrayEntity(file, contentType);
        httpPost.setEntity(entity);
        return client.executeOpen(null, httpPost, null);
    }

    /**
     * 文件上传（POST）
     *
     * @param url         链接
     * @param parameters  请求参数
     * @param headers     请求头
     * @param file        文件
     * @param contentType 类型
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse filePost(String url, Map<String, String> parameters, Map<String, Object> headers, InputStream file, ContentType contentType) throws URISyntaxException, IOException {
        HttpPost httpPost = new HttpPost(buildURI(url, parameters));
        if (headers != null) {
            headers.forEach(httpPost::addHeader);
        }
        InputStreamEntity entity = new InputStreamEntity(file, contentType);
        httpPost.setEntity(entity);
        return client.executeOpen(null, httpPost, null);
    }

    /**
     * 文件上传（POST）
     *
     * @param url         链接
     * @param headers     请求头
     * @param file        文件
     * @param contentType 类型
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse filePost(String url, Map<String, Object> headers, File file, ContentType contentType) throws URISyntaxException, IOException {
        return filePost(url, null, headers, file, contentType);
    }

    /**
     * 文件上传（POST）
     *
     * @param url         链接
     * @param headers     请求头
     * @param file        文件
     * @param contentType 类型
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse filePost(String url, Map<String, Object> headers, byte[] file, ContentType contentType) throws URISyntaxException, IOException {
        return filePost(url, null, headers, file, contentType);
    }

    /**
     * 文件上传（POST）
     *
     * @param url         链接
     * @param headers     请求头
     * @param file        文件
     * @param contentType 类型
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse filePost(String url, Map<String, Object> headers, InputStream file, ContentType contentType) throws URISyntaxException, IOException {
        return filePost(url, null, headers, file, contentType);
    }

    /**
     * 文件上传（POST）
     *
     * @param url         链接
     * @param file        文件
     * @param contentType 类型
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse filePost(String url, File file, ContentType contentType) throws URISyntaxException, IOException {
        return filePost(url, null, null, file, contentType);
    }

    /**
     * 文件上传（POST）
     *
     * @param url         链接
     * @param file        文件
     * @param contentType 类型
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse filePost(String url, byte[] file, ContentType contentType) throws URISyntaxException, IOException {
        return filePost(url, null, null, file, contentType);
    }

    /**
     * 文件上传（POST）
     *
     * @param url         链接
     * @param file        文件
     * @param contentType 类型
     * @return {@link ClassicHttpResponse} 响应
     * @author demiphea
     */
    public static ClassicHttpResponse filePost(String url, InputStream file, ContentType contentType) throws URISyntaxException, IOException {
        return filePost(url, null, null, file, contentType);
    }

    /**
     * 文件上传
     *
     * @param url         链接
     * @param parameters  请求参数
     * @param headers     请求头
     * @param file        文件
     * @param contentType 类型
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFilePost(String url, Map<String, String> parameters, Map<String, Object> headers, File file, ContentType contentType) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = filePost(url, parameters, headers, file, contentType);
        String str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();
        return str;
    }

    /**
     * 文件上传
     *
     * @param url         链接
     * @param parameters  请求参数
     * @param headers     请求头
     * @param file        文件
     * @param contentType 类型
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFilePost(String url, Map<String, String> parameters, Map<String, Object> headers, byte[] file, ContentType contentType) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = filePost(url, parameters, headers, file, contentType);
        String str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();
        return str;
    }

    /**
     * 文件上传
     *
     * @param url         链接
     * @param parameters  请求参数
     * @param headers     请求头
     * @param file        文件
     * @param contentType 类型
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFilePost(String url, Map<String, String> parameters, Map<String, Object> headers, InputStream file, ContentType contentType) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = filePost(url, parameters, headers, file, contentType);
        String str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();
        return str;
    }

    /**
     * 文件上传
     *
     * @param url         链接
     * @param headers     请求头
     * @param file        文件
     * @param contentType 类型
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFilePost(String url, Map<String, Object> headers, File file, ContentType contentType) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = filePost(url, null, headers, file, contentType);
        String str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();
        return str;
    }

    /**
     * 文件上传
     *
     * @param url         链接
     * @param headers     请求头
     * @param file        文件
     * @param contentType 类型
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFilePost(String url, Map<String, Object> headers, byte[] file, ContentType contentType) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = filePost(url, null, headers, file, contentType);
        String str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();
        return str;
    }

    /**
     * 文件上传
     *
     * @param url         链接
     * @param headers     请求头
     * @param file        文件
     * @param contentType 类型
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFilePost(String url, Map<String, Object> headers, InputStream file, ContentType contentType) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = filePost(url, null, headers, file, contentType);
        String str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();
        return str;
    }

    /**
     * 文件上传
     *
     * @param url         链接
     * @param file        文件
     * @param contentType 类型
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFilePost(String url, File file, ContentType contentType) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = filePost(url, null, null, file, contentType);
        String str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();
        return str;
    }

    /**
     * 文件上传
     *
     * @param url         链接
     * @param file        文件
     * @param contentType 类型
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFilePost(String url, byte[] file, ContentType contentType) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = filePost(url, null, null, file, contentType);
        String str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();
        return str;
    }

    /**
     * 文件上传
     *
     * @param url         链接
     * @param file        文件
     * @param contentType 类型
     * @return {@link String} 响应体
     * @author demiphea
     */
    public static String simpleFilePost(String url, InputStream file, ContentType contentType) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = filePost(url, null, null, file, contentType);
        String str = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        response.close();
        return str;
    }
}
