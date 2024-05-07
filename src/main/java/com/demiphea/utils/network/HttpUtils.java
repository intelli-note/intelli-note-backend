package com.demiphea.utils.network;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * HttpUtils
 * 网络请求工具类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Component
public class HttpUtils {
    private static CloseableHttpClient client;

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
        URIBuilder uriBuilder = new URIBuilder(url);
        if (parameters != null) {
            parameters.forEach(uriBuilder::addParameter);
        }
        HttpGet httpGet = new HttpGet(uriBuilder.build());
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
        String str = EntityUtils.toString(response.getEntity());
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
        URIBuilder uriBuilder = new URIBuilder(url);
        if (parameters != null) {
            parameters.forEach(uriBuilder::addParameter);
        }
        HttpPost httpPost = new HttpPost(uriBuilder.build());
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
     * @return {@link String} 响应
     * @author demiphea
     */
    public static String simplePost(String url, Map<String, String> parameters, Map<String, Object> headers, Object body) throws URISyntaxException, IOException, ParseException {
        ClassicHttpResponse response = post(url, parameters, headers, body);
        String str = EntityUtils.toString(response.getEntity());
        response.close();
        return str;
    }

    /**
     * 发起 POST 请求
     *
     * @param url     链接
     * @param headers 请求头
     * @param body    请求体（会被JSON序列化）
     * @return {@link String} 响应
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
     * @return {@link String} 响应
     * @author demiphea
     */
    public static String simplePost(String url, Object body) throws URISyntaxException, IOException, ParseException {
        return simplePost(url, null, null, body);
    }

    /**
     * 发起 POST 请求
     *
     * @param url 链接
     * @return {@link String} 响应
     * @author demiphea
     */
    public static String simplePost(String url) throws URISyntaxException, IOException, ParseException {
        return simplePost(url, null, null, null);
    }


    @PostConstruct
    private static void init() {
        client = HttpClients.createDefault();
    }

    @PreDestroy
    private static void destroy() throws IOException {
        client.close();
    }
}
