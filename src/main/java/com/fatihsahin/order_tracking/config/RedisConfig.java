package com.fatihsahin.order_tracking.config;

import com.fatihsahin.order_tracking.dto.UserDto.UserResponseDto;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager redisCacheManager(
            RedisConnectionFactory redisConnectionFactory) {

        JacksonJsonRedisSerializer<UserResponseDto> userSerializer =
                new JacksonJsonRedisSerializer<>(UserResponseDto.class);

        RedisCacheConfiguration usersCacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(new StringRedisSerializer())
                        )
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(userSerializer)
                        );

        return RedisCacheManager.builder(redisConnectionFactory)
                .withInitialCacheConfigurations(
                        Map.of("users", usersCacheConfiguration)
                )
                .build();
    }
}