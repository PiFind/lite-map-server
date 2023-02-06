package io.pifind.mapserver.middleware.redis.config;

import io.pifind.mapserver.middleware.redis.model.InterestPointSocialDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

    @Bean("InterestPointSocialRedisTemplate")
    public RedisTemplate<String, InterestPointSocialDTO> interestPointSocialRedisTemplate() {
        return new RedisTemplate<>();
    }

}
