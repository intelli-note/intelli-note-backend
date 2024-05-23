package com.demiphea.utils.jwt;

import cn.hutool.core.map.MapBuilder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {
    final String SK = "Test";
    final Algorithm ALGORITHM = Algorithm.HMAC256(SK);

    @Test
    void create() {
        String token = JwtUtils.create(
                MapBuilder.<String, Object>create()
                        .build(),
                7,
                ChronoUnit.DAYS,
                ALGORITHM
        );
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertFalse(token.isBlank());
    }

    @Test
    void createDefault() {
        String token = JwtUtils.createDefault(MapBuilder.<String, Object>create().build());
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertFalse(token.isBlank());
    }

    @Test
    void verify() {
        String token = JwtUtils.create(
                MapBuilder.<String, Object>create()
                        .put("str", "test")
                        .put("num", 0)
                        .build(),
                7,
                ChronoUnit.DAYS,
                ALGORITHM
        );
        assertDoesNotThrow(() -> {
            DecodedJWT decoder = JwtUtils.verify(token, ALGORITHM);
            assertEquals("test", decoder.getClaim("str").asString());
            assertEquals(0, decoder.getClaim("num").asInt());
        });
    }

    @Test
    void verifyDefault() {
        String token = JwtUtils.createDefault(
                MapBuilder.<String, Object>create()
                        .put("str", "string")
                        .put("num", 8)
                        .build()
        );
        assertDoesNotThrow(() -> {
            DecodedJWT decoder = JwtUtils.verifyDefault(token);
            assertEquals("string", decoder.getClaim("str").asString());
            assertEquals(8, decoder.getClaim("num").asInt());
        });
    }
}