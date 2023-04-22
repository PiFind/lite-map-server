package io.pifind.mapserver.open.service;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.mapserver.open.support.PoiServiceAPI;
import io.pifind.poi.model.dto.DaoVoteDTO;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 兴趣点 DAO 服务
 */
@FeignClient(name = "poi-dao-service",url = PoiServiceAPI.URL)
public interface InterestPointDaoFeignService {

    /**
     * 投票
     * @param username 投票者
     * @param voteDTO 投票信息
     * @return 投票结果
     */
    @PostMapping("/dao/review/vote")
    R<Void> vote(
            @RequestHeader("username") String username,
            @RequestBody DaoVoteDTO voteDTO
    );

    /**
     * 是否已经投过票
     * @param username 投票者
     * @param interestPointId 兴趣点ID
     * @return 是否已经投过票
     */
    @GetMapping("/dao/review/hasVoted/{interestPointId}")
    R<Boolean> hasVoted(
            @RequestHeader("username") String username,
            @PathVariable("interestPointId") Long interestPointId
    );

    /**
     * 获取待审核列表
     * @param username 用户名
     * @param administrativeAreaId 行政区划ID
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 待审核列表
     */
    @GetMapping("/dao/review/getReviewPage/{administrativeAreaId}/{currentPage}/{pageSize}")
    R<Page<InterestPointVO>> getReviewPage(
            @RequestHeader("username") String username,
            @PathVariable("administrativeAreaId") Long administrativeAreaId,
            @PathVariable("currentPage") Integer currentPage,
            @PathVariable("pageSize") Integer pageSize
    );

}
