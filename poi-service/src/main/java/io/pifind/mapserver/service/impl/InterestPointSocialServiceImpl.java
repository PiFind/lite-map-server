package io.pifind.mapserver.service.impl;

import io.pifind.common.response.R;
import io.pifind.mapserver.mapper.InterestPointMapper;
import io.pifind.mapserver.middleware.redis.model.InterestPointSocialDTO;
import io.pifind.mapserver.middleware.redis.service.InterestPointSocialRedisService;
import io.pifind.mapserver.model.po.InterestPointPO;
import io.pifind.poi.api.InterestPointSocialService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

/**
 * 兴趣点社交服务实现类
 * @see io.pifind.poi.api.InterestPointSocialService
 */
@Service
public class InterestPointSocialServiceImpl implements InterestPointSocialService {

    /**
     * 批量更新的数量
     */
    public static final int BATCH_COUNT = 1000;

    /**
     * 单位时间,单位：秒
     */
    public static final int UNIT_TIME = 600; // 10 分钟

    /**
     * 单位时间内最少的访问量
     */
    public static final int UNIT_TIME_MIN_VISITS = 300;

    @Autowired
    private InterestPointSocialRedisService interestPointSocialRedisService;

    @Autowired
    private InterestPointMapper interestPointMapper;

    /**
     * 浏览兴趣点
     * @param id 兴趣点ID
     * @return 无
     */
    @Override
    public R<Void> browse(@NotNull Long id) {
        interestPointSocialRedisService.increasePageviewsById(id,1);
        optimizeRedisStorage(id);
        return R.success();
    }

    /**
     * 收藏兴趣点
     * @param id 兴趣点ID
     * @return 无
     */
    @Override
    public R<Void> collect(@NotNull Long id) {
        interestPointSocialRedisService.increaseCollectionsById(id,1);
        optimizeRedisStorage(id);
        return R.success();
    }

    /**
     * 评价兴趣点
     * @param id 兴趣点ID
     * @param score 用户的评分
     * @return 无
     */
    @Override
    public R<Void> evaluate(@NotNull Long id, @NotNull Double score) {
        interestPointSocialRedisService.increaseScoreById(id,(int)(score + 0.5),1);
        optimizeRedisStorage(id);
        return R.success();
    }

    /**
     * 优化 Redis 存储
     * @param id 兴趣点ID
     */
    private void optimizeRedisStorage(Long id) {

        InterestPointSocialDTO interestPointSocialDTO = interestPointSocialRedisService.getInterestPointSocialById(id);

        // 检查是否为低频率数据
        if (checkLowFrequency(interestPointSocialDTO)) {

            // 同步到数据库
            ((InterestPointSocialServiceImpl)AopContext.currentProxy()).
                    synchronizeDataToDatabase(interestPointSocialDTO);

            // 删除 redis 中的数据
            interestPointSocialRedisService.deleteById(id);

        }

    }

    /**
     * 定时优化 Redis 任务，定时时间为 1分钟
     */
    @Scheduled(fixedDelay = 60000)
    private void optimizeRedisTimingTask() {
        interestPointSocialRedisService.randomDeleteInterestPointSocialByCondition(
                BATCH_COUNT,
                this::checkLowFrequencyAndSynchronizeData
        );
    }

    /**
     * 检查低频率并同步数据到数据库
     * <p>
     *     如果检测到低频率数据，那么就将数据同步到数据库
     * </p>
     * @param dto 兴趣点DTO
     * @return 返回值类型为 boolean
     * <ul>
     *     <li><b>是低频率数据</b> - 返回 {@code true}，并将数据同步到数据库中</li>
     *     <li><b>不是低频率数据</b> - 返回 {@code false}，并将数据同步到数据库中</li>
     * </ul>
     */
    public boolean checkLowFrequencyAndSynchronizeData(InterestPointSocialDTO dto) {
        boolean flag = checkLowFrequency(dto);
        if (flag) {
            // 这里可以触发事务
            ((InterestPointSocialServiceImpl)AopContext.currentProxy()).synchronizeDataToDatabase(dto);
        }
        return flag;
    }

    /**
     * 将数据同步到数据库
     * @param dto 兴趣点社交DTO数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void synchronizeDataToDatabase(InterestPointSocialDTO dto) {
        InterestPointPO po = interestPointMapper.selectById(dto.getId());
        if (po != null) {
            // 设置增量
            po.setPageviews(po.getPageviews() + dto.getPageviewsIncrement());
            po.setCollections(po.getCollections() + dto.getCollectionsIncrement());
            po.setTotalScore(po.getTotalScore() + dto.getScoreIncrement());
            po.setTotalParticipants(po.getTotalParticipants() + dto.getParticipantsIncrement());
            po.setUpdateTime(null);
            interestPointMapper.updateById(po);
        }
    }

    /**
     * 检查低频率
     * @param dto 被检查的DTO对象
     * @return 返回值类型为 boolean
     * <ul>
     *     <li><b>是低频率数据</b> - 返回 {@code true}</li>
     *     <li><b>不是低频率数据</b> - 返回 {@code false}</li>
     * </ul>
     */
    private boolean checkLowFrequency(InterestPointSocialDTO dto) {

        // 计算秒数
        int dt = (int)((System.currentTimeMillis() - dto.getCreateTimestamp()) / 1000L);

        // 如果是删除豁免时间，这段时间内不执行删除
        if ( dt < UNIT_TIME) {
            return false;
        } else {
            // 如果大于豁免时间，那么就检测其单位时间的访问量
            // 如果单位时间的访问量小于最小限制，就认为是低频访问
            int unitTimes = dt / UNIT_TIME;
            return dto.getTimes() / unitTimes < UNIT_TIME_MIN_VISITS;
        }

    }

}
