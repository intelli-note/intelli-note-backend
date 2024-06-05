package com.demiphea.utils.network;

import com.demiphea.utils.reflect.CommonReflectionUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HttpUtilsTest
 *
 * @author demiphea
 * @since 17.0.9
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@ActiveProfiles("test")
public class HttpUtilsTest {
    private static final String TEST_URL = "https://www.baidu.com";

    @BeforeAll
    static void beforeAll() {
        CommonReflectionUtils.setStaticFieldValue(HttpUtils.class,
                "client", HttpClients.createDefault()
        );
    }

    @AfterAll
    static void afterAll() throws IOException {
        CloseableHttpClient client = (CloseableHttpClient) CommonReflectionUtils.getStaticFieldValue(HttpUtils.class, "client");
        client.close();
    }

    @Test
    void get() {
        assertDoesNotThrow(() -> {
            ClassicHttpResponse response = HttpUtils.get(TEST_URL);
            assertEquals(HttpStatus.SC_OK, response.getCode());
            response.close();

            String strResponse = HttpUtils.simpleGet(TEST_URL);
            assertNotNull(strResponse);
            assertFalse(strResponse.isBlank());
        });
    }

    @Test
    void post() {
        assertDoesNotThrow(() -> {
            ClassicHttpResponse response = HttpUtils.post(TEST_URL);
            assertEquals(HttpStatus.SC_OK, response.getCode());
            response.close();

            String strResponse = HttpUtils.simplePost(TEST_URL);
            assertNotNull(strResponse);
            assertFalse(strResponse.isBlank());
        });
    }
}
