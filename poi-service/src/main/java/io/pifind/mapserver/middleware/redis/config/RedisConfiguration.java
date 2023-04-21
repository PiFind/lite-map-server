package io.pifind.mapserver.middleware.redis.config;

import io.pifind.mapserver.middleware.redis.model.InterestPointSocialDTO;
import io.pifind.mapserver.middleware.redis.model.UserVoteRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

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
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 用于存储用户投票记录的RedisTemplate
     * @return {@link RedisTemplate<String, UserVoteRecordDTO> RedisTemplate实体对象}
     */
    @Bean("UserVoteRedisTemplate")
    public RedisTemplate<String, UserVoteRecordDTO> userVoteRedisTemplate() {
        RedisTemplate<String, UserVoteRecordDTO> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

}
