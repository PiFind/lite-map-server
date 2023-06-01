package io.pifind.mapserver.controller;

import io.pifind.authorization.annotation.UserName;
import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointDaoService;
import io.pifind.poi.model.dto.DaoVoteDTO;
import io.pifind.poi.model.vo.InterestPointReviewVO;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @param username 用户名
     * @param voteDTO 投票信息
     * @return 根据code判断是否投票成功
     */
    @PostMapping("/vote")
    public R<Void> vote(
            @UserName String username,
            @RequestBody DaoVoteDTO voteDTO
    ) {
        return interestPointDaoService.vote(
                username, voteDTO
        );
    }

    /**
     * DAO用户是否已经投过票
     * @param username 用户名
     * @param interestPointId 兴趣点ID
     * @return 是否投票成功
     */
    @GetMapping("/hasVoted/{interestPointId}")
    public R<Boolean> hasVoted(
            @UserName String username,
            @PathVariable("interestPointId") Long interestPointId
    ) {
        return interestPointDaoService.hasVoted(
                username, interestPointId
        );
    }

    /**
     * 根据兴趣点id获取审核列表
     *
     * @param interestPointId 兴趣点ID
     * @return 是否投票成功
     */
    @GetMapping("/getReview/{interestPointId}")
    public R<List<InterestPointReviewVO>> getReview(@PathVariable("interestPointId") Long interestPointId) {
        return interestPointDaoService.getReviewList(interestPointId);
    }

    /**
     * 获取当前用户已审核/已拒绝的兴趣点列表
     *
     * @param username
     * @param agree
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/getReviewList")
    public R<Page<InterestPointReviewVO>> getReviewList(@UserName String username,
                                                        @RequestParam(value = "agree", defaultValue = "true") Boolean agree,
                                                        @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return interestPointDaoService.getReviewList(username, agree, currentPage, pageSize);
    }

    /**
     * DAO用户获取待审核列表
     * @param username 用户名
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 待审核列表
     */
    @GetMapping("/getReviewPage/{administrativeAreaId}/{currentPage}/{pageSize}")
    public R<Page<InterestPointVO>> getReviewPage(
            @UserName String username,
            @PathVariable("administrativeAreaId") String administrativeAreaId,
            @PathVariable("currentPage") Integer currentPage,
            @PathVariable("pageSize") Integer pageSize) {
        return interestPointDaoService.getReviewPage(
                username, administrativeAreaId,currentPage, pageSize
        );
    }

}
