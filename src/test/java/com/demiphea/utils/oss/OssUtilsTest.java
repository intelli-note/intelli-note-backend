package com.demiphea.utils.oss;

import cn.hutool.crypto.digest.DigestUtil;
import com.google.gson.Gson;
import com.qiniu.storage.model.DefaultPutRet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OssUtilsTest {
    private static final String ACCESS_KEY = "mock-ak";
    private static final String SECRET_KEY = "mock-sk";
    private static final String BUCKET = "mock-bucket";
    private static final String DOMAIN = "www.mock-url.com";

    static MockedStatic<OssUtils> mockOssUtils;

    @BeforeAll
    static void beforeAll() throws NoSuchFieldException, IllegalAccessException {
        mockOssUtils = mockStatic(OssUtils.class, Answers.CALLS_REAL_METHODS);
        Field context = OssUtils.class.getDeclaredField("context");
        context.setAccessible(true);
        context.set(
                null,
                new OssProperty(ACCESS_KEY, SECRET_KEY, BUCKET, DOMAIN)
        );
    }

    @AfterAll
    static void afterAll() {
        mockOssUtils.close();
        mockOssUtils = null;
    }

    static String createKey(List<String> directory, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method createKey = OssUtils.class.getDeclaredMethod("createKey", List.class, String.class);
        createKey.setAccessible(true);
        return (String) (createKey.invoke(
                null,
                directory,
                name
        ));
    }

    static String createUrl(String key) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method createUrl = OssUtils.class.getDeclaredMethod("createUrl", String.class);
        createUrl.setAccessible(true);
        return (String) (createUrl.invoke(
                null,
                key
        ));
    }

    static String parseKey(String url) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method parseKey = OssUtils.class.getDeclaredMethod("parseKey", String.class);
        parseKey.setAccessible(true);
        return (String) (parseKey.invoke(
                null,
                url
        ));
    }

    @Test
    void upload() throws IOException {
        final List<String> directory = List.of("mock-directory");
        final String filename = "mock-filename";
        MockMultipartFile file = new MockMultipartFile("mock-file.txt", "mock-file.txt", "text/plain", "mock-file-content".getBytes());

        Answer<String> answer = invocation -> {
            String key = createKey(
                    invocation.getArgument(0, List.class),
                    invocation.getArgument(1, String.class)
            );
            String hash = "mock-hash";
            Object _file = invocation.getArgument(2);
            assertNotNull(_file);
            if (_file instanceof byte[]) {
                hash = DigestUtil.sha256Hex((byte[]) _file);
            } else if (_file instanceof InputStream) {
                hash = DigestUtil.sha256Hex((InputStream) _file);
            } else if (_file instanceof MultipartFile) {
                hash = DigestUtil.sha256Hex(((MultipartFile) _file).getInputStream());
            }

            DefaultPutRet putRet = new Gson().fromJson(
                    "{ hash: \"" + hash + "\", key: \"" + key + "\" }",
                    DefaultPutRet.class
            );

            return createUrl(putRet.key);
        };

        // mock upload by byte array
        mockOssUtils
                .when(() -> OssUtils.upload(anyList(), anyString(), any(byte[].class)))
                .then(answer);
        assertEquals(
                "https://" + DOMAIN + "/mock-directory/mock-filename",
                OssUtils.upload(directory, filename, file.getBytes())
        );

        // mock upload by input-stream
        mockOssUtils
                .when(() -> OssUtils.upload(anyList(), anyString(), any(InputStream.class)))
                .then(answer);
        assertEquals(
                "https://" + DOMAIN + "/mock-directory/mock-filename",
                OssUtils.upload(directory, filename, file.getInputStream())
        );

        // mock upload by multipart-file
        mockOssUtils
                .when(() -> OssUtils.upload(anyList(), anyString(), any(MultipartFile.class)))
                .then(answer);
        assertEquals(
                "https://" + DOMAIN + "/mock-directory/mock-filename",
                OssUtils.upload(directory, filename, file)
        );

        // mock upload with non-ful parameter
        mockOssUtils
                .when(() -> OssUtils.upload(isNull(), anyString(), any(InputStream.class)))
                .then(answer);
        assertEquals(
                "https://" + DOMAIN + "/mock-filename",
                OssUtils.upload(null, filename, file.getInputStream())
        );
        mockOssUtils
                .when(() -> OssUtils.upload(anyList(), isNull(), any(InputStream.class)))
                .then(answer);
        assertDoesNotThrow(() -> OssUtils.upload(directory, null, file.getInputStream()));
        mockOssUtils
                .when(() -> OssUtils.upload(isNull(), isNull(), any(InputStream.class)))
                .then(answer);
        assertDoesNotThrow(() -> OssUtils.upload(null, null, file.getInputStream()));
    }

    @Test
    void delete() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        final List<String> directory = List.of("mock-directory");
        final String filename = "mock-filename";

        assertEquals(
                "mock-directory/mock-filename",
                createKey(directory, filename)
        );
    }

    @Test
    void deleteByUrl() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        final String url = "https://" + DOMAIN + "/mock-key";

        assertEquals(
                "mock-key",
                parseKey(url)
        );
    }
}