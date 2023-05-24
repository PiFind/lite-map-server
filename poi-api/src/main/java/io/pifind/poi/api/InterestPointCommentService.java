package io.pifind.poi.api;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.model.dto.InterestPointCommentDTO;
import io.pifind.poi.model.vo.InterestPointCommentVO;

/**
 * 兴趣点评论服务
 */
public interface InterestPointCommentService {

    /**
     * 获取评论
     * @param id 评论ID
     * @return 评论
     */
    R<InterestPointCommentVO> getCommentById(Long id);

    /**
     * 获取评论分页
     * @param interestPointId 兴趣点ID
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 评论分页
     */
    R<Page<InterestPointCommentVO>> getCommentPage(
            Long interestPointId,
            Integer currentPage,
            Integer pageSize
    );

    /**
     * 评论点赞
     * @param username 用户名
     * @param id 评论ID
     * @return 无
     */
    R<Void> likeComment(String username,Long id);

    /**
     * 发表评论
     * @param username 用户名
     * @param dto 评论信息
     * @return 无
     */
    R<Void> postComment(String username,InterestPointCommentDTO dto);

    /**
     * 修改评论
     * @param username 用户名
     * @param dto 评论信息
     * @return 无
     */
    R<Void> modifyComment(String username,InterestPointCommentDTO dto);

    /**
     * 删除评论
     * @param username 用户名
     * @param commentId 评论ID
     * @return 无
     */
    R<Void> removeComment(String username,Long commentId);

}
