package io.pifind.mapserver.service.impl;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointCommentDaoService;
import io.pifind.poi.model.dto.DaoCommentModerationDTO;
import io.pifind.poi.model.vo.InterestPointCommentVO;
import org.springframework.stereotype.Service;

@Service
public class InterestPointCommentDaoServiceImpl implements InterestPointCommentDaoService {

    /**
     * 审核评论
     * @param dto 审核信息
     * @return 无
     */
    @Override
    public R<Void> reviewComment(DaoCommentModerationDTO dto) {


        return null;
    }

    /**
     * 获取待审核评论分页
     * @param administrativeAreaId 行政区划ID
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 待审核评论分页
     */
    @Override
    public R<Page<InterestPointCommentVO>> getPendingCommentPage(
            Long administrativeAreaId,
            Integer currentPage,
            Integer pageSize
    ) {


        return null;
    }

    /**
     * 删除评论
     * @param commentId 评论ID
     * @return 无
     */
    @Override
    public R<Void> removeComment(Long commentId) {
        return null;
    }
}
