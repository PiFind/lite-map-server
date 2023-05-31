package io.pifind.poi.api;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.model.dto.DaoCommentModerationDTO;
import io.pifind.poi.model.vo.InterestPointCommentVO;

/**
 * 评论审核DAO服务
 */
public interface InterestPointCommentDaoService {

    /**
     * 审核评论
     * @param dto 审核信息
     * @return 无
     */
    R<Void> reviewComment(DaoCommentModerationDTO dto);

    /**
     * 获取待审核评论分页
     * @param administrativeAreaId 行政区划ID
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 待审核评论分页
     */
    R<Page<InterestPointCommentVO>> getPendingCommentPage(
            String administrativeAreaId,
            Integer currentPage,
            Integer pageSize
    );

    /**
     * 删除评论
     * @param commentId 评论ID
     * @return 无
     */
    R<Void> removeComment(Long commentId);

}
