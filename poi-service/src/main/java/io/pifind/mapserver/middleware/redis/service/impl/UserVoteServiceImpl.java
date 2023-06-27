package io.pifind.mapserver.middleware.redis.service.impl;

import io.pifind.mapserver.middleware.redis.model.UserVoteRecordDTO;
import io.pifind.mapserver.middleware.redis.service.IUserVoteService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户投票服务实现类
 */
@Service
public class UserVoteServiceImpl implements IUserVoteService {

    /**
     * DAO 用户投票 Redis键生成器
     */
    private static final String VOTE_PREFIX = "DaoUserVote_";

    @Resource(name = "userVoteRedisTemplate")
    private RedisTemplate<String, UserVoteRecordDTO> userVoteRedisTemplate;

    /**
     * 判断用户是否已经投票
     * @param username 用户名
     * @param interestPointId 兴趣点ID
     * @return 用户是否已经投票
     */
    @Override
    public Boolean hasVoted(String username, Long interestPointId) {
        String key  = getVoteKey(interestPointId);
        UserVoteRecordDTO userVoteRecord = userVoteRedisTemplate.opsForValue().get(key);
        boolean result = false;
        if (Objects.nonNull(userVoteRecord) && !CollectionUtils.isEmpty(userVoteRecord.getUserVoteMap())) {
            result = userVoteRecord.getUserVoteMap().containsKey(username);
        }
        return result;
    }

    /**
     * 投票
     * @param username 用户名
     * @param interestPointId 兴趣点ID
     * @param agree 是否同意
     */
    @Override
    public UserVoteRecordDTO vote(String username, Long interestPointId, Boolean agree) {
        UserVoteRecordDTO voteRecord = getVoteRecord(interestPointId);
        if (Objects.nonNull(voteRecord)) {
            // 如果存在投票记录,那么检查是否已经投过票了
            boolean isVoted = false;
            if (!CollectionUtils.isEmpty(voteRecord.getUserVoteMap())) {
                isVoted = voteRecord.getUserVoteMap().containsKey(username);
            }
            if (isVoted) {
                return voteRecord;
            }

            // 如果没有投过票,那么更新投票记录
            Map<String, Boolean> userVoteMap = voteRecord.getUserVoteMap();
            userVoteMap.put(username, agree);
            voteRecord.setUserVoteMap(userVoteMap);
            voteRecord.setTotal(voteRecord.getTotal() + 1);
            voteRecord.setAgrees(agree ? voteRecord.getAgrees() + 1 : voteRecord.getAgrees());
            userVoteRedisTemplate.opsForValue().set(getVoteKey(interestPointId), voteRecord);
        } else {
            // 如果不存在投票记录,那么创建一个新的投票记录
            voteRecord = new UserVoteRecordDTO();
            HashMap<String, Boolean> userVoteMap = new HashMap<>();
            userVoteMap.put(username, agree);
            voteRecord.setUserVoteMap(userVoteMap);
            voteRecord.setTotal(1);
            voteRecord.setAgrees(agree ? 1 : 0);
            userVoteRedisTemplate.opsForValue().set(getVoteKey(interestPointId), voteRecord);
        }
        return voteRecord;
    }

    /**
     * 移除投票记录
     * @param interestPointId 兴趣点ID
     */
    @Override
    public void remove(Long interestPointId) {
        // 实现方式为设置过期时间,时间为 10 分钟
        userVoteRedisTemplate.expire(VOTE_PREFIX + interestPointId, 10, TimeUnit.MINUTES);
    }

    public static String getVoteKey(Long interestPointId) {
        return VOTE_PREFIX + interestPointId;
    }

    /**
     * 获取投票记录
     * @param interestPointId 兴趣点ID
     * @return 投票记录
     */
    @Override
    public UserVoteRecordDTO getVoteRecord(Long interestPointId) {
        return userVoteRedisTemplate.opsForValue().get(VOTE_PREFIX + interestPointId);
    }

}
