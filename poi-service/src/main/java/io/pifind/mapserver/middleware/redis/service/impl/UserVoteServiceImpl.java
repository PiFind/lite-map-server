package io.pifind.mapserver.middleware.redis.service.impl;

import io.pifind.mapserver.middleware.redis.model.UserVoteRecordDTO;
import io.pifind.mapserver.middleware.redis.service.IUserVoteService;
import io.pifind.mapserver.redis.FormattedStringKeyGenerator;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户投票服务实现类
 */
@Service
public class UserVoteServiceImpl implements IUserVoteService {

    /**
     * DAO 用户投票 Redis键生成器
     */
    private static final FormattedStringKeyGenerator KEY_GENERATOR = FormattedStringKeyGenerator.create("DaoUserVote@%d");

    @Resource(name = "UserVoteRedisTemplate")
    private RedisTemplate<String, UserVoteRecordDTO> userVoteRedisTemplate;

    /**
     * 判断用户是否已经投票
     * @param username 用户名
     * @param interestPointId 兴趣点ID
     * @return 用户是否已经投票
     */
    @Override
    public Boolean hasVoted(String username, Long interestPointId) {
        SessionCallback<Boolean> callback = new SessionCallback<Boolean>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {

                // (1) 开始事务
                operations.multi();
                boolean flag = false;

                // (2) 检查是否存在投票记录
                String key  =KEY_GENERATOR.generate(interestPointId);
                if (Boolean.TRUE.equals(operations.hasKey(key))) {
                    // 如果存在投票记录,那么获取该投票纪录
                    UserVoteRecordDTO userVoteRecordDTO =
                            (UserVoteRecordDTO) operations.opsForValue().get(key);
                    // 检查是否已经投过票了
                    flag = userVoteRecordDTO.getUserVoteMap().containsKey(username);
                }

                // (3) 执行事务
                operations.exec();

                return flag;
            }
        };

        return userVoteRedisTemplate.execute(callback);
    }

    /**
     * 投票
     * @param username 用户名
     * @param interestPointId 兴趣点ID
     * @param agree 是否同意
     */
    @Override
    public UserVoteRecordDTO vote(String username, Long interestPointId, Boolean agree) {

        // 创建一个会话事务
        SessionCallback<UserVoteRecordDTO> callback = new SessionCallback<UserVoteRecordDTO>() {
            @Override
            public UserVoteRecordDTO execute(RedisOperations operations) throws DataAccessException {

                // (1) 开始事务
                operations.multi();
                UserVoteRecordDTO userVoteRecordDTO = null;

                // (2) 检查是否存在投票记录
                String key  =KEY_GENERATOR.generate(interestPointId);
                if (Boolean.TRUE.equals(operations.hasKey(key))) {
                    // 如果存在投票记录,那么检查是否已经投过票了
                    if (hasVoted(username, interestPointId)) {
                        // 如果已经投过票了,那么直接返回
                        userVoteRecordDTO = (UserVoteRecordDTO) operations.opsForValue().get(key);
                    } else {
                        // 如果没有投过票,那么更新投票记录
                        userVoteRecordDTO = (UserVoteRecordDTO) operations.opsForValue().get(key);
                        Map<String, Boolean> userVoteMap = userVoteRecordDTO.getUserVoteMap();
                        userVoteMap.put(username, agree);
                        userVoteRecordDTO.setUserVoteMap(userVoteMap);
                        userVoteRecordDTO.setTotal(userVoteRecordDTO.getTotal() + 1);
                        userVoteRecordDTO.setAgrees(agree?userVoteRecordDTO.getAgrees() + 1:userVoteRecordDTO.getAgrees());
                        operations.opsForValue().set(key, userVoteRecordDTO);
                    }
                } else {
                    // 如果不存在投票记录,那么创建一个新的投票记录
                    userVoteRecordDTO = new UserVoteRecordDTO();
                    HashMap<String, Boolean> userVoteMap = new HashMap<>();
                    userVoteMap.put(username, agree);
                    userVoteRecordDTO.setUserVoteMap(userVoteMap);
                    userVoteRecordDTO.setTotal(1);
                    userVoteRecordDTO.setAgrees(agree?1:0);
                    operations.opsForValue().set(key, userVoteRecordDTO);
                }

                // (3) 执行事务
                operations.exec();
                return userVoteRecordDTO;
            }
        };

        // 返回数据
        return userVoteRedisTemplate.execute(callback);
    }

    /**
     * 移除投票记录
     * @param interestPointId 兴趣点ID
     */
    @Override
    public void remove(Long interestPointId) {
        // 实现方式为设置过期时间,时间为 10 分钟
        userVoteRedisTemplate.expire(
                KEY_GENERATOR.generate(interestPointId),
                10,
                TimeUnit.SECONDS
        );
    }

    /**
     * 获取投票记录
     * @param interestPointId 兴趣点ID
     * @return 投票记录
     */
    @Override
    public UserVoteRecordDTO getVoteRecord(Long interestPointId) {
        return userVoteRedisTemplate.opsForValue().get(KEY_GENERATOR.generate(interestPointId));
    }

}
