package io.pifind.mapserver.middleware.redis.service;

import io.pifind.mapserver.middleware.redis.model.UserVoteRecordDTO;

public interface IUserVoteService {

    /**
     * 用户是否已经投过票了
     * @param username 用户名
     * @param interestPointId 兴趣点ID
     * @return 用户是否已经投过票了
     */
    Boolean hasVoted(String username, Long interestPointId);

    /**
     * 投票
     * @param username 用户名
     * @param interestPointId 兴趣点ID
     * @param agree 是否同意
     */
    UserVoteRecordDTO vote(String username, Long interestPointId, Boolean agree);

    /**
     * 移除投票记录
     * @param interestPointId 兴趣点ID
     */
    void remove(Long interestPointId);

    /**
     * 获取投票记录
     * @param interestPointId 兴趣点ID
     * @return 投票记录
     */
    UserVoteRecordDTO getVoteRecord(Long interestPointId);

}
