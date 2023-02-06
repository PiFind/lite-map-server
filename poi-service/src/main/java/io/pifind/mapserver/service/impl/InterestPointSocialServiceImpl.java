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

@Service
public class InterestPointSocialServiceImpl implements InterestPointSocialService {

    public static final int BATCH_COUNT = 1000;

    /**
     * 豁免时间,单位：秒
     */
    public static final int RELEASED_TIME = 600; // 10 分钟

    @Autowired
    private InterestPointSocialRedisService interestPointSocialRedisService;

    @Autowired
    private InterestPointMapper interestPointMapper;

    @Override
    public R<Void> browse(@NotNull Long id) {
        interestPointSocialRedisService.increasePageviewsById(id,1);
        optimizeRedisStorage(id);
        return R.success();
    }

    @Override
    public R<Void> collect(@NotNull Long id) {
        interestPointSocialRedisService.increaseCollectionsById(id,1);
        optimizeRedisStorage(id);
        return R.success();
    }

    @Override
    public R<Void> evaluate(@NotNull Long id, @NotNull Double score) {
        interestPointSocialRedisService.increaseScoreById(id,(int)(score + 0.5),1);
        optimizeRedisStorage(id);
        return R.success();
    }

    private void optimizeRedisStorage(Long id) {

        InterestPointSocialDTO interestPointSocialDTO = interestPointSocialRedisService.getInterestPointSocialById(id);

        // 检查是否为低频率数据
        if (checkLowFrequency(interestPointSocialDTO)) {

            // 删除 redis 中的数据
            interestPointSocialRedisService.deleteById(id);

            // 同步到数据库
            ((InterestPointSocialServiceImpl)AopContext.currentProxy()).
                    synchronizeDataToDatabase(interestPointSocialDTO);

        }

    }

    /**
     * 定时优化 Redis 任务，定时时间为 1分钟
     */
    @Scheduled(fixedDelay = 60000)
    private void optimizeRedisTimingTask() {
        interestPointSocialRedisService.randomDeleteInterestPointSocialByCondition(
                BATCH_COUNT,
                this::checkLowFrequencyAndSynchronizeData);
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
        if ( dt < RELEASED_TIME ) {
            return false;
        } else {

            // 如果大于豁免时间，那么就检测其日均访问量
            // 如果日均访问小于 1000，就认为是低频访问
            if ((dt / 86400) >= 1) {
                return dto.getTimes() / dt < 1000;
            }

        }

        return false;
    }

}
