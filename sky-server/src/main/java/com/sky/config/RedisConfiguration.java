package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建redis模板对象");
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 创建字符串序列化器（用于 key）
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 创建 JSON 序列化器（用于 value）
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        // key 使用字符串序列化
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash 的 key 使用字符串序列化
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value 使用 JSON 序列化
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        // hash 的 value 使用 JSON 序列化
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);

        return redisTemplate;
    }
}
