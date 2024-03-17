package com.demiphea.config.redis;

import com.alibaba.fastjson2.support.spring6.data.redis.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * RedisConfig
 *
 * @author demiphea
 * @since 17.0.9
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();

//        // 设置key序列化器
//        template.setKeySerializer(stringRedisSerializer);
//        // 设置value序列化器
//        template.setValueSerializer(fastJsonRedisSerializer);
//        // 设置hash key序列化器
//        template.setHashKeySerializer(stringRedisSerializer);
//        // 设置hash value序列化器
//        template.setHashValueSerializer(fastJsonRedisSerializer);

        template.setDefaultSerializer(fastJsonRedisSerializer);

        return template;
    }
}
