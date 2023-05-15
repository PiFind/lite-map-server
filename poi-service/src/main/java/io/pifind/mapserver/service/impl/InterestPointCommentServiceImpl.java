package io.pifind.mapserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.mapserver.converter.po.InterestPointCommentPoConverter;
import io.pifind.mapserver.converter.po.InterestPointPoConverter;
import io.pifind.mapserver.converter.vo.InterestPointCommentVoConverter;
import io.pifind.mapserver.mapper.InterestPointCommentMapper;
import io.pifind.mapserver.middleware.rocketmq.MQProducerService;
import io.pifind.mapserver.middleware.rocketmq.constant.PoiServiceRocketMQConstants;
import io.pifind.mapserver.middleware.rocketmq.model.PendingMachineAuditCommentDTO;
import io.pifind.mapserver.model.constant.InterestPointCommentStatusEnum;
import io.pifind.mapserver.model.po.InterestPointCommentPO;
import io.pifind.mapserver.mp.page.MybatisPage;
import io.pifind.poi.api.InterestPointCommentService;
import io.pifind.poi.model.dto.InterestPointCommentDTO;
import io.pifind.poi.model.vo.InterestPointCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InterestPointCommentServiceImpl implements InterestPointCommentService {

    @Autowired
    private MQProducerService mqProducerService;

    @Autowired
    private InterestPointCommentVoConverter interestPointCommentVoConverter;

    @Autowired
    private InterestPointCommentPoConverter interestPointCommentPoConverter;

    @Autowired
    private InterestPointCommentMapper interestPointCommentMapper;

    /**
     * 获取评论
     * @param id 评论ID
     * @return 评论
     */
    @Override
    public R<InterestPointCommentVO> getCommentById(Long id) {

        // (1) 根据ID获取评论
        InterestPointCommentPO comment = interestPointCommentMapper.selectById(id);

        // (2) 如果不存在评论那么返回失败
        if (comment == null) {
            return R.failure();
        }

        InterestPointCommentVO commentVO =
                interestPointCommentVoConverter.convert(comment);

        // (3) 获取其子评论
        commentVO.setSubComments(getSubCommentsById(id));

        return R.success(commentVO);
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

        // (1) 获取一个分页的评论数据
        MybatisPage<InterestPointCommentPO> commentPage = new MybatisPage<>(currentPage,pageSize);
        interestPointCommentMapper.selectPage(
                commentPage,
                new LambdaQueryWrapper<InterestPointCommentPO>()
                        .eq(InterestPointCommentPO::getInterestPointId,interestPointId)
                        .orderByDesc(InterestPointCommentPO::getLikes)
        );

        // (2) 填充子评论
        List<InterestPointCommentVO> commentVoList = new ArrayList<>();
        List<InterestPointCommentPO> comments = commentPage.getRecords();
        for (InterestPointCommentPO commentPO : comments) {
            InterestPointCommentVO commentVO =
                    interestPointCommentVoConverter.convert(commentPO);
            commentVO.setSubComments(getSubCommentsById(commentPO.getId()));
            commentVoList.add(commentVO);
        }

        return R.page(
                (int) commentPage.getCurrent(),
                (int) commentPage.getSize(),
                (int) commentPage.getTotal(),
                commentVoList
        );
    }

    /**
     * 评论点赞
     * @param username 用户名
     * @param id 评论ID
     * @return 无
     */
    @Override
    public R<Void> likeComment(String username,Long id) {

        // (1) 根据ID获取评论
        InterestPointCommentPO comment = interestPointCommentMapper.selectById(id);

        // (2) 如果不存在评论那么返回失败
        if (comment == null) {
            return R.failure();
        }

        // (3) 点赞 + 1
        InterestPointCommentPO modifiedComment = new InterestPointCommentPO();
        modifiedComment.setId(comment.getId());
        modifiedComment.setLikes(modifiedComment.getLikes() + 1);
        interestPointCommentMapper.updateById(modifiedComment);

        return R.success();
    }

    /**
     * 发表评论
     * @param username 用户名
     * @param dto 评论信息
     * @return 无
     */
    @Override
    public R<Void> postComment(String username, InterestPointCommentDTO dto) {

        // (1) 先进行转换
        InterestPointCommentPO commentPO =
                interestPointCommentPoConverter.convert(dto);

        Long commentId = IdWorker.getId();
        commentPO.setId(commentId);
        commentPO.setLikes(0);
        commentPO.setUsername(username);
        commentPO.setStatus(InterestPointCommentStatusEnum.PENDING_MACHINE_AUDIT);

        // (2) 添加到数据库
        interestPointCommentMapper.insert(commentPO);

        // (3) 推送到机审消费者
        PendingMachineAuditCommentDTO pendingMachineAuditCommentDTO = new PendingMachineAuditCommentDTO();
        pendingMachineAuditCommentDTO.setCommentId(commentId);
        pendingMachineAuditCommentDTO.setCreateTime(new Date());
        mqProducerService.send(
                PoiServiceRocketMQConstants.MACHINE_AUDIT_COMMENT_TOPIC,
                pendingMachineAuditCommentDTO
                );

        return R.success();
    }

    /**
     * 修改评论
     * @param username 用户名
     * @param dto 评论信息
     * @return 无
     */
    @Override
    public R<Void> modifyComment(String username, InterestPointCommentDTO dto) {

        // (1) 获取评论 ID
        Long commentId = dto.getId();

        // (2) 根据ID获取评论
        InterestPointCommentPO comment = interestPointCommentMapper.selectById(commentId);

        // (3) 如果不存在评论那么返回失败
        if (comment == null) {
            return R.failure();
        }

        // (4) 检查发布者是不是当前用户
        if (comment.getUsername().equals(username)) {
            return R.failure();
        }

        // (5) 更改内容并更新数据库
        InterestPointCommentPO modifiedComment = new InterestPointCommentPO();
        modifiedComment.setInterestPointId(commentId);
        modifiedComment.setContent(dto.getContent());
        modifiedComment.setStatus(InterestPointCommentStatusEnum.PENDING_MACHINE_AUDIT);
        interestPointCommentMapper.updateById(modifiedComment);

        // (6) 推送到机审消费者
        PendingMachineAuditCommentDTO pendingMachineAuditCommentDTO = new PendingMachineAuditCommentDTO();
        pendingMachineAuditCommentDTO.setCommentId(commentId);
        pendingMachineAuditCommentDTO.setCreateTime(comment.getCreateTime());
        mqProducerService.send(
                PoiServiceRocketMQConstants.MACHINE_AUDIT_COMMENT_TOPIC,
                pendingMachineAuditCommentDTO
        );

        return R.success();
    }

    /**
     * 删除评论
     * @param username 用户名
     * @param commentId 评论ID
     * @return 无
     */
    @Override
    public R<Void> removeComment(String username, Long commentId) {

        // (1) 根据ID获取评论
        InterestPointCommentPO comment = interestPointCommentMapper.selectById(commentId);

        // (2) 如果不存在评论那么返回失败
        if (comment == null) {
            return R.failure();
        }

        // (3) 检查发布者是不是当前用户
        if (comment.getUsername().equals(username)) {
            return R.failure();
        }

        // (4) 从数据库删除数据
        interestPointCommentMapper.deleteById(commentId);

        return R.success();
    }

    /**
     * 获取子评论
     * @param id 当前评论ID
     * @return 当前评论的子评论列表
     */
    private List<InterestPointCommentVO> getSubCommentsById(Long id) {

        // (1) 查询并获取子评论
        List<InterestPointCommentPO> comments =
                interestPointCommentMapper.selectList(
                        new LambdaQueryWrapper<InterestPointCommentPO>()
                                .eq(InterestPointCommentPO::getSuperiorId,id)
                );

        // (2) 进行递归获取子评论的子评论
        if (comments == null || comments.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<InterestPointCommentVO> commentVoList = new ArrayList<>();
            for (InterestPointCommentPO commentPO : comments) {
                InterestPointCommentVO commentVO =
                        interestPointCommentVoConverter.convert(commentPO);
                commentVO.setSubComments(getSubCommentsById(commentPO.getId()));
                commentVoList.add(commentVO);
            }
            return commentVoList;
        }

    }
}
