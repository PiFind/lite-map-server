package io.pifind.mapserver.middleware.redis.config;

import io.pifind.mapserver.middleware.redis.model.InterestPointSocialDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

    /**
     * 用于存储兴趣点社交信息的RedisTemplate
     * @return {@link RedisTemplate<String, InterestPointSocialDTO> RedisTemplate实体对象}
     */
    @Bean("InterestPointSocialRedisTemplate")
    public RedisTemplate<String, InterestPointSocialDTO> interestPointSocialRedisTemplate() {
        return new RedisTemplate<>();
    }

}
