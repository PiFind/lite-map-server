package io.pifind.mapserver.controller;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointCommentService;
import io.pifind.poi.model.dto.InterestPointCommentDTO;
import io.pifind.poi.model.vo.InterestPointCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/poi/dao/comment")
public class InterestPointCommentDaoController {

    @Autowired
    private InterestPointCommentService interestPointCommentService;

    /**
     * 获取评论
     * @param id 评论ID
     * @return 评论
     */
    @GetMapping("/get/{id}")
    public R<InterestPointCommentVO> getCommentById(
            @PathVariable("id") Long id
    ) {
        return interestPointCommentService.getCommentById(id);
    }

    /**
     * 获取评论分页
     * @param interestPointId 兴趣点ID
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 评论分页
     */
    @GetMapping("/page/{interestPointId}/{currentPage}/{pageSize}")
    public R<Page<InterestPointCommentVO>> getCommentPage(
            @PathVariable("interestPointId") Long interestPointId,
            @PathVariable("currentPage") Integer currentPage,
            @PathVariable("pageSize") Integer pageSize
    ) {
        return interestPointCommentService.getCommentPage(
                interestPointId,
                currentPage,
                pageSize
        );
    }

    /**
     * 评论点赞
     * @param username 用户名
     * @param id 评论ID
     * @return 无
     */
    @GetMapping("/like")
    public R<Void> likeComment(
            @RequestHeader("username") String username,
            @PathVariable("id") Long id) {
        return interestPointCommentService.likeComment(username,id);
    }

    /**
     * 发表评论
     * @param username 用户名
     * @param dto 评论信息
     * @return 无
     */
    @PostMapping("/add")
    public R<Void> postComment(
            @RequestHeader("username") String username,
            @RequestBody InterestPointCommentDTO dto
    ) {
        return interestPointCommentService.postComment(username,dto);
    }

    /**
     * 修改评论
     * @param username 用户名
     * @param dto 评论信息
     * @return 无
     */
    @PostMapping("/modify")
    public R<Void> modifyComment(
            @RequestHeader("username") String username,
            @RequestBody InterestPointCommentDTO dto
    ) {
        return interestPointCommentService.modifyComment(username,dto);
    }

    /**
     * 删除评论
     * @param username 用户名
     * @param commentId 评论ID
     * @return 无
     */
    @DeleteMapping("/remove/{commentId}")
    public R<Void> removeComment(
            @RequestHeader("username") String username,
            @PathVariable("commentId") Long commentId
    ) {
        return interestPointCommentService.removeComment(username,commentId);
    }

}
