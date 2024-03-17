package com.demiphea.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

@Configuration
public class JacksonConfig {
    /**
     * 解决数据库类型到前端精度丢失的问题
     * 前端使用String来解析
     * 后端使用Long/Double/Big保证效率
     */
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 全局配置序列化返回 JSON 处理
        SimpleModule simpleModule = new SimpleModule();

        //JSON：Long/Double ==> String
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Double.class, ToStringSerializer.instance);
        // JSON：Big ==> String
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);

        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

}