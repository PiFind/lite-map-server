package io.pifind.mapserver.middleware.rocketmq.consumer;


import io.pifind.mapserver.mapper.InterestPointCommentMapper;
import io.pifind.mapserver.middleware.rocketmq.constant.PoiServiceRocketMQConstants;
import io.pifind.mapserver.middleware.rocketmq.model.PendingMachineAuditCommentDTO;
import io.pifind.mapserver.model.constant.InterestPointCommentStatusEnum;
import io.pifind.mapserver.model.po.InterestPointCommentPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 机审评论消费者
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = PoiServiceRocketMQConstants.MACHINE_AUDIT_COMMENT_TOPIC,
        consumerGroup = PoiServiceRocketMQConstants.GROUP,
        selectorType = SelectorType.TAG,
        selectorExpression = "*"
)
public class MachineAuditCommentConsumer implements RocketMQListener<PendingMachineAuditCommentDTO> {

    @Autowired
    private InterestPointCommentMapper interestPointCommentMapper;

    @Override
    @Transactional
    public void onMessage(PendingMachineAuditCommentDTO pendingMachineAuditCommentDTO) {

        // (1) 获取评论 ID，并获取数据库中的评论数据
        InterestPointCommentPO interestPointCommentPO =
                interestPointCommentMapper.selectById(pendingMachineAuditCommentDTO.getCommentId());

        // (2) 检测评论是否为空，及评论当前状态
        if (interestPointCommentPO == null ||
                !interestPointCommentPO.getStatus().equals(InterestPointCommentStatusEnum.PENDING_MACHINE_AUDIT)) {
            return;
        }

        // TODO (3) 进行机审
        boolean flag = true;

        // (4) 设置状态并更新数据
        InterestPointCommentPO modifiedCommentPO = new InterestPointCommentPO();
        modifiedCommentPO.setId(interestPointCommentPO.getId());
        modifiedCommentPO.setStatus(
                flag? InterestPointCommentStatusEnum.MACHINE_AUDIT_PASS:InterestPointCommentStatusEnum.MACHINE_AUDIT_REFUSE
        );
        interestPointCommentMapper.updateById(interestPointCommentPO);
        
    }

}
