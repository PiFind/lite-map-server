package io.pifind.mapserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.mapserver.converter.vo.InterestPointCommentVoConverter;
import io.pifind.mapserver.mapper.InterestPointCommentMapper;
import io.pifind.mapserver.model.constant.InterestPointCommentStatusEnum;
import io.pifind.mapserver.model.po.InterestPointCommentPO;
import io.pifind.mapserver.mp.page.MybatisPage;
import io.pifind.poi.api.InterestPointCommentDaoService;
import io.pifind.poi.model.dto.DaoCommentModerationDTO;
import io.pifind.poi.model.vo.InterestPointCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterestPointCommentDaoServiceImpl implements InterestPointCommentDaoService {

    @Autowired
    private InterestPointCommentMapper interestPointCommentMapper;

    @Autowired
    private InterestPointCommentVoConverter interestPointCommentVoConverter;

    /**
     * 审核评论
     * @param dto 审核信息
     * @return 无
     */
    @Override
    public R<Void> reviewComment(DaoCommentModerationDTO dto) {

        // (1) 获取评论
        InterestPointCommentPO commentPO =
                interestPointCommentMapper.selectById(dto.getId());

        // (2) 进行校验
        if (!commentPO.getInterestPointId().equals(dto.getInterestPointCommentId())) {
            return R.failure();
        }

        // (3) 判断是否通过
        if (dto.getPass()) {
            commentPO.setStatus(
                    InterestPointCommentStatusEnum.PASS
            );
        } else {
            commentPO.setStatus(
                    InterestPointCommentStatusEnum.REFUSE
            );
        }

        return R.success();
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

        // (1) 设置评论分页
        MybatisPage<InterestPointCommentPO> commentPage =
                new MybatisPage<>(currentPage,pageSize);

        // (2) 获取待审核评论
        interestPointCommentMapper.selectPage(
                commentPage,
                new LambdaQueryWrapper<InterestPointCommentPO>()
                        .eq(InterestPointCommentPO::getAdministrativeAreaId,administrativeAreaId)
                        .eq(InterestPointCommentPO::getStatus, InterestPointCommentStatusEnum.PENDING)
        );

        // (3) 返回待审核页
        return R.page(
                (int) commentPage.getCurrent(),
                (int) commentPage.getSize(),
                (int) commentPage.getTotal(),
                interestPointCommentVoConverter.convert(commentPage.getRecords())
        );

    }

    /**
     * 删除评论
     * @param commentId 评论ID
     * @return 无
     */
    @Override
    public R<Void> removeComment(Long commentId) {

        // (1) 获取评论
        InterestPointCommentPO commentPO =
                interestPointCommentMapper.selectById(commentId);

        // (2) 如果不存在，则返回失败
        if (commentPO == null) {
            return R.failure();
        }

        // (3) 删除评论
        interestPointCommentMapper.deleteById(commentId);

        return R.success();
    }
}
