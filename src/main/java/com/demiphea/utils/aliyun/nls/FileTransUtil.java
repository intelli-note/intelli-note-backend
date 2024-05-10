package com.demiphea.utils.aliyun.nls;

import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.demiphea.exception.utils.aliyun.ApiResponseCodeStatusException;
import com.demiphea.exception.utils.aliyun.ApiServiceCodeStatusException;
import com.demiphea.utils.aliyun.AliyunProfile;
import com.demiphea.utils.aliyun.PopApiCenter;
import com.demiphea.utils.json.JsonObjectBuilder;
import org.apache.hc.core5.http.HttpStatus;

import java.util.concurrent.TimeUnit;

/**
 * FileTransUtil
 * 录音文件识别API
 *
 * <p>
 * 详情见 <a href="https://help.aliyun.com/zh/isi/developer-reference/api-reference-2?spm=a2c4g.11186623.0.0.ec084e47qGZn6P">接口说明</a>
 * </p>
 *
 * @author demiphea
 * @since 17.0.9
 */
public class FileTransUtil {
    private static final String product = PopApiCenter.NLS_FILETRANS.product;
    private static final String regionId = PopApiCenter.NLS_FILETRANS.regionId;
    private static final String endpointName = PopApiCenter.NLS_FILETRANS.endpointName;
    private static final String domain = PopApiCenter.NLS_FILETRANS.domain;

    private static AliyunProfile aliyunProfile;
    private static String appKey;
    private static IAcsClient client;

    private static IAcsClient client() {
        if (client == null) {
            DefaultProfile.addEndpoint(regionId, product, endpointName);
            DefaultProfile profile = DefaultProfile.getProfile(regionId, aliyunProfile.getAccessKey(), aliyunProfile.getSecretKey());
            client = new DefaultAcsClient(profile);
        }
        return client;
    }

    private static CommonRequest buildPostRequest(JSONObject task) {
        CommonRequest postRequest = new CommonRequest();
        postRequest.setSysDomain(domain);
        postRequest.setSysVersion("2018-08-17");
        postRequest.setSysAction("SubmitTask");
        postRequest.setSysProduct(product);
        postRequest.putBodyParameter("Task", task.toJSONString());
        postRequest.setSysMethod(MethodType.POST);
        postRequest.setHttpContentType(FormatType.JSON);
        return postRequest;
    }

    private static CommonRequest buildGetRequest(String taskId) {
        CommonRequest getRequest = new CommonRequest();
        getRequest.setSysDomain(domain);
        getRequest.setSysVersion("2018-08-17");
        getRequest.setSysAction("GetTaskResult");
        getRequest.setSysProduct(product);
        getRequest.putQueryParameter("TaskId", taskId);
        getRequest.setSysMethod(MethodType.GET);
        return getRequest;
    }

    /**
     * 提交录音文件
     *
     * @param fileUrl 文件链接
     * @return {@link JSONObject} 包含TaskId的JSON
     * @author demiphea
     */
    public static JSONObject submit(String fileUrl) throws ClientException {
        JSONObject task = JsonObjectBuilder.create()
                .put("appkey", appKey)
                .put("file_link", fileUrl)
                .put("version", "4.0")
                .put("enable_sample_rate_adaptive", true)
                .build();

        CommonRequest postRequest = buildPostRequest(task);
        CommonResponse response = client().getCommonResponse(postRequest);
        if (response.getHttpStatus() != HttpStatus.SC_OK) {
            throw new ApiResponseCodeStatusException("调用接口失败，响应码：" + response.getHttpStatus());
        }
        JSONObject result = JSONObject.parseObject(response.getData());
        int statusCode = result.getIntValue("StatusCode");
        if (Status.SUCCESS.code != statusCode) {
            throw new ApiServiceCodeStatusException("提交失败(" + statusCode + "): " + result.getString("StatusText"));
        }
        return result;
    }

    /**
     * 解析响应结果
     *
     * @param res 响应字符串
     * @return {@link JSONObject} 识别结果
     * @author demiphea
     */
    public static JSONObject parseResult(String res) {
        JSONObject result = JSONObject.parseObject(res);
        int statusCode = result.getIntValue("StatusCode");
        if (
                Status.SUCCESS.code != statusCode
                        && Status.QUEUEING.code != statusCode
                        && Status.RUNNING.code != statusCode
        ) {
            throw new ApiServiceCodeStatusException("识别失败(" + statusCode + "): " + result.getString("StatusText"));
        }
        return result;
    }

    /**
     * 获取识别结果
     *
     * @param taskId TaskId
     * @return {@link JSONObject} 识别结果
     * @author demiphea
     */
    public static JSONObject getResult(String taskId) throws ClientException {
        CommonRequest getRequest = buildGetRequest(taskId);
        CommonResponse response = client().getCommonResponse(getRequest);
        if (response.getHttpStatus() != HttpStatus.SC_OK) {
            throw new ApiResponseCodeStatusException("调用接口失败，响应码：" + response.getHttpStatus());
        }
        return parseResult(response.getData());
    }

    /**
     * 轮询识别录音文件
     *
     * @param fileUrl  录音文件
     * @param amount   轮询时间
     * @param timeUnit 轮询时间单位
     * @return {@link JSONObject}  识别结果
     * @author demiphea
     */
    public static JSONObject polling(String fileUrl, long amount, TimeUnit timeUnit) throws ClientException, InterruptedException {
        JSONObject submitResponse = submit(fileUrl);
        CommonRequest getRequest = buildGetRequest(submitResponse.getString("TaskId"));
        long interval = TimeUnit.MILLISECONDS.convert(amount, timeUnit);
        while (true) {
            CommonResponse response = client().getCommonResponse(getRequest);
            if (response.getHttpStatus() != HttpStatus.SC_OK) {
                throw new ApiResponseCodeStatusException("调用接口失败，响应码：" + response.getHttpStatus());
            }
            JSONObject result = JSONObject.parseObject(response.getData());
            int statusCode = result.getIntValue("StatusCode");
            if (Status.QUEUEING.code == statusCode || Status.RUNNING.code == statusCode) {
                Thread.sleep(interval);
            } else {
                if (statusCode != Status.SUCCESS.code) {
                    throw new ApiServiceCodeStatusException("识别失败(" + statusCode + "): " + result.getString("StatusText"));
                }
                return result;
            }
        }
    }

    /**
     * 回调识别录音文件
     *
     * @param fileUrl     文件链接
     * @param callbackUrl 回调地址
     * @return {@link JSONObject} 接口调用结果
     * @author demiphea
     */
    public static JSONObject callback(String fileUrl, String callbackUrl) throws ClientException {
        JSONObject task = JsonObjectBuilder.create()
                .put("appkey", appKey)
                .put("file_link", fileUrl)
                .put("version", "4.0")
                .put("enable_sample_rate_adaptive", true)
                .put("enable_callback", true)
                .put("callback_url", callbackUrl)
                .build();

        CommonRequest postRequest = buildPostRequest(task);
        CommonResponse response = client().getCommonResponse(postRequest);
        if (response.getHttpStatus() != HttpStatus.SC_OK) {
            throw new ApiResponseCodeStatusException("调用接口失败，响应码：" + response.getHttpStatus());
        }
        JSONObject result = JSONObject.parseObject(response.getData());
        int statusCode = result.getIntValue("StatusCode");
        if (Status.SUCCESS.code != statusCode) {
            throw new ApiServiceCodeStatusException("提交失败(" + statusCode + "): " + result.getString("StatusText"));
        }
        return result;
    }

    /**
     * 响应业务状态
     *
     * @author demiphea
     * @since 17.0.9
     */
    private enum Status {
        /**
         * 成功
         */
        SUCCESS(21050000),
        /**
         * 排队中
         */
        QUEUEING(21050002),
        /**
         * 识别中
         */
        RUNNING(21050001);
        /**
         * 状态码
         */
        public final int code;

        Status(int code) {
            this.code = code;
        }
    }

    /**
     * 支持识别的单轨和双轨的录音文件类型
     *
     * @author demiphea
     * @since 17.0.9
     */
    public enum SupportFileType {
        WAV,
        MP3,
        MP4,
        M4A,
        WMA,
        AAC,
        OGG,
        AMR,
        FLAC,
        ;
    }

}
