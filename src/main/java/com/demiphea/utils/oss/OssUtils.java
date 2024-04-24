package com.demiphea.utils.oss;

import cn.hutool.core.util.RandomUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.*;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * OssUtils
 * 七牛云Oss工具类
 *
 * @author demiphea
 * @since 17.0.9
 */
@Component
public class OssUtils {
    private static OssProperty context;
    private static Auth auth;
    private static Configuration configuration;

    private static String createKey(List<String> directory, String name) {
        StringBuilder key = new StringBuilder();
        if (directory != null && !directory.isEmpty()) {
            for (String dir : directory) {
                if (!dir.isBlank()) {
                    key.append(dir).append("/");
                }
            }
        }
        if (name != null && !name.isBlank()) {
            return key.append(name).toString();
        }
        return key.append(RandomUtil.randomString(10)).append("_").append(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
        ).toString();
    }

    private static String parseKey(String url) {
        return url.replaceFirst("https://" + context.getDomain() + "/", "");
    }

    private static String createUrl(String key) throws QiniuException {
        return new DownloadUrl(context.getDomain(), true, key).buildURL();
    }

    /**
     * 上传文件（已弃用）
     *
     * @param directory 目录
     * @param name      文件名
     * @param file      文件字节数组
     * @return {@link String} 存储资源url
     * @author demiphea
     */
    @Deprecated
    public static String upload(List<String> directory, String name, byte[] file) throws QiniuException {
        UploadManager uploadManager = new UploadManager(configuration);
        DefaultPutRet putRet = new Gson().fromJson(
                uploadManager.put(file, createKey(directory, name), auth.uploadToken(context.getBucket())).bodyString(),
                DefaultPutRet.class
        );
        return createUrl(putRet.key);
    }

    /**
     * 上传文件
     *
     * @param directory 目录
     * @param name      文件名
     * @param file      文件流
     * @return {@link String} 存储资源url
     * @author demiphea
     */
    public static String upload(List<String> directory, String name, InputStream file) throws QiniuException {
        UploadManager uploadManager = new UploadManager(configuration);
        DefaultPutRet putRet = new Gson().fromJson(
                uploadManager.put(file, createKey(directory, name), auth.uploadToken(context.getBucket()), null, null).bodyString(),
                DefaultPutRet.class
        );
        return createUrl(putRet.key);

    }

    /**
     * 上传文件
     *
     * @param directory 目录
     * @param name      文件名
     * @param file      文件
     * @return {@link String} 存储资源url
     * @author demiphea
     */
    public static String upload(List<String> directory, String name, MultipartFile file) throws IOException {
        UploadManager uploadManager = new UploadManager(configuration);
        DefaultPutRet putRet = new Gson().fromJson(
                uploadManager.put(file.getInputStream(), createKey(directory, name), auth.uploadToken(context.getBucket()), null, null).bodyString(),
                DefaultPutRet.class
        );
        return createUrl(putRet.key);
    }

    private static void delete(String key) throws QiniuException {
        BucketManager bucketManager = new BucketManager(auth, configuration);
        bucketManager.delete(context.getBucket(), key);
    }

    /**
     * 删除文件
     *
     * @param directory 目录
     * @param name      文件名
     * @author demiphea
     */
    public static void delete(List<String> directory, String name) throws QiniuException {
        BucketManager bucketManager = new BucketManager(auth, configuration);
        bucketManager.delete(context.getBucket(), createKey(directory, name));
    }

    /**
     * 删除文件
     *
     * @param url 文件链接
     * @author demiphea
     */
    public static void deleteByUrl(String url) throws QiniuException {
        BucketManager bucketManager = new BucketManager(auth, configuration);
        bucketManager.delete(context.getBucket(), parseKey(url));
    }

    @Autowired
    private void setContext(OssProperty context) {
        OssUtils.context = context;
        OssUtils.auth = Auth.create(context.getAccessKey(), context.getSecretKey());
        OssUtils.configuration = new Configuration(Region.createWithRegionId("cn-east-2"));
        OssUtils.configuration.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
    }
}
