package com.demiphea.utils.oss.qiniu;

import cn.hutool.core.util.RandomUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
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
public final class OssUtils {
    private static OssProfile profile;
    private static Auth auth;
    private static Configuration configuration;

    /**
     * 创建文件存储键
     *
     * @param directory 目录
     * @param name      文件名
     * @return {@link String} 存储键
     * @author demiphea
     */
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

    /**
     * 从存储url中解析文件存储键
     * @param url 存储url
     * @return {@link String} 文件存储键
     * @author demiphea
     */
    private static String parseKey(String url) {
        return url.replaceFirst("https://" + profile.getDomain() + "/", "");
    }

    /**
     * 构建存储url
     * @param key 文件存储键
     * @return {@link String} 存储url
     * @author demiphea
     */
    private static String createUrl(String key) throws QiniuException {
        return new DownloadUrl(profile.getDomain(), true, key).buildURL();
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
                uploadManager.put(file, createKey(directory, name), auth.uploadToken(profile.getBucket())).bodyString(),
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
                uploadManager.put(file, createKey(directory, name), auth.uploadToken(profile.getBucket()), null, null).bodyString(),
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
                uploadManager.put(file.getInputStream(), createKey(directory, name), auth.uploadToken(profile.getBucket()), null, null).bodyString(),
                DefaultPutRet.class
        );
        return createUrl(putRet.key);
    }

    private static void delete(String key) throws QiniuException {
        BucketManager bucketManager = new BucketManager(auth, configuration);
        bucketManager.delete(profile.getBucket(), key);
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
        bucketManager.delete(profile.getBucket(), createKey(directory, name));
    }

    /**
     * 删除文件
     *
     * @param url 文件链接
     * @author demiphea
     */
    public static void deleteByUrl(String url) throws QiniuException {
        BucketManager bucketManager = new BucketManager(auth, configuration);
        bucketManager.delete(profile.getBucket(), parseKey(url));
    }
}
