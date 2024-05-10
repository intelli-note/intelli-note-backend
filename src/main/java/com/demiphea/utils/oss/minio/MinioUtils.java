package com.demiphea.utils.oss.minio;

import cn.hutool.core.util.RandomUtil;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * MinioUtils
 * MinioOss工具类
 *
 * @author demiphea
 * @since 17.0.9
 */
public class MinioUtils {
    private static MinioClient client;
    private static String bucket;
    private static String url;

    private static String parseContentType(InputStream stream) throws IOException {
        return new Tika().detect(stream);
    }

    private static String parseContentType(InputStream stream, String name) throws IOException {
        return new Tika().detect(stream, name);
    }

    /**
     * 创建文件存储键
     *
     * @param directory 文件目录
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

    private static PutObjectArgs putObjectArgs(
            List<String> directory,
            String name,
            Map<String, String> userMetadata,
            Map<String, String> tags,
            InputStream stream,
            long objectSize,
            long partSize
    ) throws IOException {
        InputStream packStream = stream;
        if (!stream.markSupported()) {
            packStream = new BufferedInputStream(stream);
        }
        String contentType = name == null ? parseContentType(packStream) : parseContentType(packStream, name);
        return PutObjectArgs.builder()
                .bucket(bucket)
                .object(createKey(directory, name))
                .userMetadata(userMetadata)
                .tags(tags)
                .stream(packStream, objectSize, partSize)
                .contentType(contentType)
                .build();
    }

    private static PutObjectArgs putObjectArgs(List<String> directory, String name, InputStream stream) throws IOException {
        InputStream packStream = stream;
        if (!stream.markSupported()) {
            packStream = new BufferedInputStream(stream);
        }
        String contentType = name == null ? parseContentType(packStream) : parseContentType(packStream, name);
        return PutObjectArgs.builder()
                .bucket(bucket)
                .object(createKey(directory, name))
                .stream(packStream, packStream.available(), -1)
                .contentType(contentType)
                .build();
    }

    private static PutObjectArgs putObjectArgs(List<String> directory, String name, MultipartFile file) throws IOException {
        return PutObjectArgs.builder()
                .bucket(bucket)
                .object(createKey(directory, name))
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
    }

    /**
     * 添加对象
     *
     * @param directory    目录
     * @param name         对象名
     * @param userMetadata 元数据
     * @param tags         标签
     * @param stream       对象流
     * @param objectSize   对象数据大小
     * @param partSize     分段大小
     * @return {@link String} 对象目标地址
     * @author demiphea
     */
    public static String put(
            List<String> directory,
            String name,
            Map<String, String> userMetadata,
            Map<String, String> tags,
            InputStream stream,
            long objectSize,
            long partSize
    ) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        ObjectWriteResponse response = client.putObject(putObjectArgs(directory, name, userMetadata, tags, stream, objectSize, partSize));
        return url + response.object();
    }

    /**
     * 添加对象
     *
     * @param directory 目录
     * @param name      对象名
     * @param stream    对象流
     * @return {@link String} 对象目标地址
     * @author demiphea
     */
    public static String put(List<String> directory, String name, InputStream stream) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        ObjectWriteResponse response = client.putObject(putObjectArgs(directory, name, stream));
        return url + response.object();
    }

    /**
     * 添加对象
     *
     * @param directory 目录
     * @param name      对象名
     * @param file      文件
     * @return {@link String} 对象目标地址
     * @author demiphea
     */
    public static String put(List<String> directory, String name, MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        ObjectWriteResponse response = client.putObject(putObjectArgs(directory, name, file));
        return url + response.object();
    }
}
