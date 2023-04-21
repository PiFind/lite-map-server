package io.pifind.mapserver.controller;

import io.pifind.authorization.annotation.UserEntity;
import io.pifind.authorization.model.User;
import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointDaoService;
import io.pifind.poi.model.dto.DaoVoteDTO;
import io.pifind.poi.model.vo.InterestPointVO;
import io.pifind.role.annotation.RequestPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * DAO用户审核控制器
 */
@RestController
@RequestMapping("/v1/poi/dao/review")
public class InterestPointDaoController {

    @Autowired
    private InterestPointDaoService interestPointDaoService;

    /**
     * DAO用户投票
     * @param user 用户信息
     * @param voteDTO 投票信息
     * @return 根据code判断是否投票成功
     */
    @PostMapping("/vote")
    @RequestPermission(name = "poi.dao.review.vote",description = "DAO用户投票")
    public R<Void> vote(
            @UserEntity User user,
            @RequestBody DaoVoteDTO voteDTO
    ) {
        return interestPointDaoService.vote(
                user.getUsername(), voteDTO
        );
    }

    /**
     * DAO用户是否已经投过票
     * @param user 用户信息
     * @param interestPointId 兴趣点ID
     * @return 是否投票成功
     */
    @GetMapping("/hasVoted/{interestPointId}")
    @RequestPermission(name = "poi.dao.review.hasVoted",description = "DAO用户是否已经投过票")
    public R<Boolean> hasVoted(
            @UserEntity User user,
            @PathVariable("interestPointId") Long interestPointId
    ) {
        return interestPointDaoService.hasVoted(
                user.getUsername(), interestPointId
        );
    }

    /**
     * DAO用户获取待审核列表
     * @param user 用户信息
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 待审核列表
     */
    @GetMapping("/getReviewPage/{administrativeAreaId}/{currentPage}/{pageSize}")
    @RequestPermission(name = "poi.dao.review.getReviewPage",description = "DAO用户获取待审核列表")
    public R<Page<InterestPointVO>> getReviewPage(
            @UserEntity User user,
            @PathVariable("administrativeAreaId") Long administrativeAreaId,
            @PathVariable("currentPage") Integer currentPage,
            @PathVariable("pageSize") Integer pageSize) {
        return interestPointDaoService.getReviewPage(
                user.getUsername(), administrativeAreaId,currentPage, pageSize
        );
    }

}
