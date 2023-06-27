package io.pifind.mapserver.middleware.redis.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import io.pifind.mapserver.middleware.redis.model.InterestPointSocialDTO;
import io.pifind.mapserver.middleware.redis.model.UserVoteRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 用于存储兴趣点社交信息的RedisTemplate
     * @return {@link RedisTemplate<String, InterestPointSocialDTO> RedisTemplate实体对象}
     */
    @Bean("InterestPointSocialRedisTemplate")
    public RedisTemplate<String, InterestPointSocialDTO> interestPointSocialRedisTemplate() {
        RedisTemplate<String, InterestPointSocialDTO> redisTemplate = new RedisTemplate<>();
        StringRedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 用于存储用户投票记录的RedisTemplate
     * @return {@link RedisTemplate<String, UserVoteRecordDTO> RedisTemplate实体对象}
     */
    @Bean("userVoteRedisTemplate")
    public RedisTemplate<String, UserVoteRecordDTO> userVoteRedisTemplate() {
        RedisTemplate<String, UserVoteRecordDTO> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

}
