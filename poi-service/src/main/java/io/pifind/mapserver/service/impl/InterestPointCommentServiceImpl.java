package io.pifind.mapserver.service.impl;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointCommentService;
import io.pifind.poi.model.dto.InterestPointCommentDTO;
import io.pifind.poi.model.vo.InterestPointCommentVO;
import org.springframework.stereotype.Service;

@Service
public class InterestPointCommentServiceImpl implements InterestPointCommentService {

    /**
     * 获取评论
     * @param id 评论ID
     * @return 评论
     */
    @Override
    public R<InterestPointCommentVO> getCommentById(Long id) {
        return null;
    }

    /**
     * 获取评论分页
     * @param interestPointId 兴趣点ID
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 评论分页
     */
    @Override
    public R<Page<InterestPointCommentVO>> getCommentPage(
            Long interestPointId,
            Integer currentPage,
            Integer pageSize
    ) {


        return null;
    }

    /**
     * 发表评论
     * @param username 用户名
     * @param dto 评论信息
     * @return 无
     */
    @Override
    public R<Void> postComment(String username, InterestPointCommentDTO dto) {


        return null;
    }

    /**
     * 修改评论
     * @param username 用户名
     * @param dto 评论信息
     * @return 无
     */
    @Override
    public R<Void> modifyComment(String username, InterestPointCommentDTO dto) {


        return null;
    }

    /**
     * 删除评论
     * @param username 用户名
     * @param commentId 评论ID
     * @return 无
     */
    @Override
    public R<Void> removeComment(String username, Long commentId) {


        return null;
    }

}
