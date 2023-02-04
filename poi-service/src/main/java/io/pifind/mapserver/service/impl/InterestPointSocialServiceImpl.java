package io.pifind.mapserver.service.impl;

import io.pifind.common.response.R;
import io.pifind.mapserver.mapper.InterestPointMapper;
import io.pifind.mapserver.middleware.redis.model.InterestPointSocialDTO;
import io.pifind.mapserver.middleware.redis.service.InterestPointSocialRedisService;
import io.pifind.mapserver.model.po.InterestPointPO;
import io.pifind.poi.api.InterestPointSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class InterestPointSocialServiceImpl implements InterestPointSocialService {

    public static final int BATCH_COUNT = 1000;

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
            // 如果是低频率数据，那么同步到数据库
            synchronizeDataToDatabase(interestPointSocialDTO);
            // 删除 redis 中的数据
            interestPointSocialRedisService.deleteById(id);
        }
    }

    /**
     * 将数据同步到数据库
     * @param dto 兴趣点社交DTO数据
     */
    @Transactional(rollbackFor = Exception.class)
    private void synchronizeDataToDatabase(InterestPointSocialDTO dto) {
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
        // 当前时间到创建时间 > 3天
        if (System.currentTimeMillis() - dto.getCreateTimestamp() > 259200000L) {
            // 访问次数 < 1000 次，就认为是低频访问
            if (dto.getTimes() < 1000) {
                return true;
            }
        } else {
            // 如果大于 3 天，就认为它在三天内访问到了 1000
            // 检查其日访问量是否能达到 1000 ，如果不能那就认为是低频访问
            int dt = (int) ((System.currentTimeMillis() - dto.getCreateTimestamp()) / 86400000L);
            if (dt >= 1) {
                if (dto.getTimes()/dt >= 1000) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 定时优化 Redis 任务，定时时间为 1分钟
     */
    @Scheduled(fixedDelay = 60000)
    private void optimizeRedisTimingTask() {
        List<InterestPointSocialDTO> batch =
                interestPointSocialRedisService.randomInterestPointSocialDtoList(BATCH_COUNT);
        for (InterestPointSocialDTO dto : batch) {
            checkLowFrequency(dto);
        }
    }

}
