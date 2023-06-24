package io.pifind.poi.api;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.model.dto.DaoVoteDTO;
import io.pifind.poi.model.vo.InterestPointReviewVO;
import io.pifind.poi.model.vo.InterestPointVO;

import java.util.List;

/**
 * 兴趣点 DAO 用户服务
 */
public interface InterestPointDaoService {

    /**
     * 投票
     * @param username 投票者
     * @param voteDTO 投票信息
     * @return 投票结果
     */
    R<Void> vote(String username, DaoVoteDTO voteDTO);

    /**
     * 是否已经投过票
     * @param username 投票者
     * @param interestPointId 兴趣点ID
     * @return 是否已经投过票
     */
    R<Boolean> hasVoted(String username, Long interestPointId);

    /**
     * 获取待审核列表
     * @param username 用户名
     * @param locale 语言
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 待审核列表
     */
    R<Page<InterestPointVO>> getReviewPage(String username, String locale, Integer currentPage, Integer pageSize);

    /**
     * 获取审核数据列表
     *
     * @param interestPointId
     * @return
     */
    R<List<InterestPointReviewVO>> getReviewList(Long interestPointId);

    /**
     * 获取当前用户已审核/已拒绝的兴趣点列表
     *
     * @param username
     * @param currentPage
     * @param pageSize
     * @return
     */
    R<Page<InterestPointReviewVO>> getReviewList(String username, Integer currentPage, Integer pageSize);
}