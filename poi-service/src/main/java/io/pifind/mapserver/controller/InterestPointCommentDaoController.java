package io.pifind.mapserver.controller;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointCommentDaoService;
import io.pifind.poi.model.dto.DaoCommentModerationDTO;
import io.pifind.poi.model.vo.InterestPointCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/poi/dao/comment")
public class InterestPointCommentDaoController {

    @Autowired
    private InterestPointCommentDaoService interestPointCommentDaoService;

    /**
     * 审核评论
     * @param dto 审核信息
     * @return 无
     */
    @PostMapping("/review")
    public R<Void> reviewComment(
            @RequestBody DaoCommentModerationDTO dto
    ) {
        return interestPointCommentDaoService.reviewComment(dto);
    }

    /**
     * 获取待审核评论分页
     * @param administrativeAreaId 行政区划ID
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 待审核评论分页
     */
    @GetMapping("/getPendingCommentPage/{administrativeAreaId}/{currentPage}/{pageSize}")
    public R<Page<InterestPointCommentVO>> getPendingCommentPage(
            @PathVariable("administrativeAreaId") Long administrativeAreaId,
            @PathVariable("currentPage") Integer currentPage,
            @PathVariable("pageSize") Integer pageSize
    ) {
        return interestPointCommentDaoService.getPendingCommentPage(
                administrativeAreaId,
                currentPage,
                pageSize
        );
    }

    /**
     * 删除评论
     * @param commentId 评论ID
     * @return 无
     */
    @DeleteMapping("/remove/{commentId}")
    public R<Void> removeComment(@PathVariable("commentId") Long commentId) {
        return interestPointCommentDaoService.removeComment(commentId);
    }

}
