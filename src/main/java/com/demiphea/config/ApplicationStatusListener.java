package com.demiphea.config;

import com.alibaba.nls.client.AccessToken;
import com.demiphea.utils.aliyun.AliyunProfile;
import com.demiphea.utils.aliyun.nls.FileTransUtil;
import com.demiphea.utils.aliyun.nls.NLSProfile;
import com.demiphea.utils.aliyun.nls.SpeechFlashRecognizerUtil;
import com.demiphea.utils.network.HttpUtils;
import com.demiphea.utils.oss.minio.MinioProfile;
import com.demiphea.utils.oss.minio.MinioUtils;
import com.demiphea.utils.oss.qiniu.OssProfile;
import com.demiphea.utils.oss.qiniu.OssUtils;
import com.demiphea.utils.reflect.CommonReflectionUtils;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ApplicationStatusListener
 * 应用状态监听器（负责监听应用开启后/销毁前）
 *
 * @author demiphea
 * @since 17.0.9
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationStatusListener {
    private final OssProfile ossProfile;
    private final AliyunProfile aliyunProfile;
    private final NLSProfile nlsProfile;
    private final MinioProfile minioProfile;

    @PostConstruct
    private void postApplicationStart() {
        initOssUtils();
        initMinioUtils();
        initHttpUtils();
        initAliyunUtils();
    }

    @PreDestroy
    private void preApplicationDestroy() throws Exception {
        destroyMinioUtils();
        destroyHttpUtils();
    }

    private void initOssUtils() {
        log.info("Init OssUtils...");
        Class<OssUtils> clazz = OssUtils.class;
        CommonReflectionUtils.setStaticFieldValue(clazz,
                "profile", ossProfile
        );
        CommonReflectionUtils.setStaticFieldValue(clazz,
                "auth", Auth.create(ossProfile.getAccessKey(), ossProfile.getSecretKey())
        );
        Configuration configuration = new Configuration(Region.createWithRegionId(ossProfile.getRegion()));
        configuration.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        CommonReflectionUtils.setStaticFieldValue(clazz,
                "configuration", configuration
        );
    }

    private void initMinioUtils() {
        log.info("Init MinioUtils...");
        Class<MinioUtils> clazz = MinioUtils.class;
        MinioClient client = MinioClient.builder()
                .endpoint(minioProfile.getEndpoint())
                .credentials(minioProfile.getAccessKey(), minioProfile.getSecretKey())
                .build();
        CommonReflectionUtils.setStaticFieldValue(clazz,
                "client", client
        );
        CommonReflectionUtils.setStaticFieldValue(clazz,
                "bucket", minioProfile.getBucket()
        );
        CommonReflectionUtils.setStaticFieldValue(clazz,
                "url", minioProfile.getEndpoint() + "/" + minioProfile.getBucket() + "/"
        );
    }

    private void destroyMinioUtils() throws Exception {
        log.info("Destroy MinioUtils...");
        MinioClient client = (MinioClient) CommonReflectionUtils.getStaticFieldValue(MinioUtils.class, "client");
        client.close();
    }

    private void initHttpUtils() {
        log.info("Init HttpUtils...");
        CommonReflectionUtils.setStaticFieldValue(HttpUtils.class,
                "client", HttpClients.createDefault()
        );
    }

    private void destroyHttpUtils() throws IOException {
        log.info("Destroy HttpUtils...");
        CloseableHttpClient client = (CloseableHttpClient) CommonReflectionUtils.getStaticFieldValue(HttpUtils.class, "client");
        client.close();
    }

    private void initAliyunUtils() {
        log.info("Init SpeechFlashRecognizerUtil...");
        Class<SpeechFlashRecognizerUtil> sfruClass = SpeechFlashRecognizerUtil.class;
        CommonReflectionUtils.setStaticFieldValue(sfruClass,
                "accessToken", new AccessToken(aliyunProfile.getAccessKey(), aliyunProfile.getSecretKey())
        );
        CommonReflectionUtils.setStaticFieldValue(sfruClass,
                "appKey", nlsProfile.getAppKey()
        );

        log.info("Init FileTransUtil...");
        Class<FileTransUtil> ftuClass = FileTransUtil.class;
        CommonReflectionUtils.setStaticFieldValue(ftuClass,
                "appKey", nlsProfile.getAppKey()
        );
        CommonReflectionUtils.setStaticFieldValue(ftuClass,
                "aliyunProfile", aliyunProfile
        );
    }

}
