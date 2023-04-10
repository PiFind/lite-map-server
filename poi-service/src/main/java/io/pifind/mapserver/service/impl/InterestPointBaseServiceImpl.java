package io.pifind.mapserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.pifind.common.response.R;
import io.pifind.mapserver.converter.vo.InterestPointVoConverter;
import io.pifind.mapserver.converter.po.InterestPointPoConverter;
import io.pifind.mapserver.error.PoiCodeEnum;
import io.pifind.mapserver.mapper.InterestPointMapper;
import io.pifind.mapserver.middleware.redis.service.InterestPointSocialRedisService;
import io.pifind.mapserver.model.po.InterestPointPO;
import io.pifind.poi.api.InterestPointBaseService;
import io.pifind.poi.model.dto.InterestPointDTO;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;

import javax.validation.constraints.NotNull;

/**
 * 兴趣点基础服务实现类
 * @see io.pifind.poi.api.InterestPointBaseService
 */
@Service
public class InterestPointBaseServiceImpl implements InterestPointBaseService {

    @Autowired
    private InterestPointVoConverter interestPointVoConverter;

    @Autowired
    private InterestPointPoConverter interestPointPoConverter;

    @Autowired
    private InterestPointMapper interestPointMapper;

    @Autowired
    private InterestPointSocialRedisService interestPointSocialRedisService;

    /**
     * 添加兴趣点
     * @param interestPoint {@link InterestPointDTO 兴趣点实体对象}
     * @return 无
     */
    @Override
    public R<Void> addInterestPoint(@NotNull InterestPointDTO interestPoint) {

        // (1) 将 DTO 对象转换成 PO 对象
        InterestPointPO po = interestPointPoConverter.convert(interestPoint);

        // (2) 根据PO对象计算 Hash，并设置到 PO 对象中
        String hash = hash(po);
        po.setHash(hash);

        // (3) 幂等检查（检查是否有Hash值一样的数据）
        if (interestPointMapper.exists(new LambdaQueryWrapper<InterestPointPO>().eq(InterestPointPO::getHash,hash))) {
            return R.failure(PoiCodeEnum.DUPLICATE_POI_DATA);
        }

        // (4) 加入到数据库中
        interestPointMapper.insert(po);

        return R.success();

    }

    /**
     * 根据兴趣点ID获取兴趣点信息
     * @param id 兴趣点ID
     * @return {@link InterestPointVO 兴趣点实体对象}
     */
    @Override
    public R<InterestPointVO> getInterestPointById(@NotNull Long id) {

        InterestPointPO po = interestPointMapper.selectById(id);
        if (po == null) {
            return R.failure(PoiCodeEnum.POI_DATA_NOT_FOUND);
        }
        InterestPointVO vo = interestPointVoConverter.convert(po);

        /*
         * 通过 redis 获取当前真正的浏览量、收藏量、评分等
         */

        // 浏览量
        int pageviewsInc = interestPointSocialRedisService.getPageviewsIncrementById(id);
        vo.setPageviews(pageviewsInc + vo.getPageviews());

        // 收藏量
        int collectionsInc = interestPointSocialRedisService.getCollectionsIncrementById(id);
        vo.setCollections(collectionsInc + vo.getPageviews());

        // 评分
        int scoreInc = interestPointSocialRedisService.getScoreIncrementById(id);
        int participantsInc = interestPointSocialRedisService.getParticipantsIncrementById(id);
        int realScore = po.getTotalScore() + po.getHiddenScore() + scoreInc;
        int realParticipants = po.getTotalParticipants() + participantsInc;
        // 计算平均分
        double score = realScore/(double)realParticipants;
        if (score > 5.0) {
            score = 5;
        }
        vo.setScore(Double.parseDouble(String.format("%.1f",score)));

        return R.success(vo);
    }

    /**
     * 根据兴趣点ID删除兴趣点
     * @param modifiedInterestPoint 修改过兴趣点信息后的{@link InterestPointDTO 兴趣点实体对象}
     * @return 无
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R<Void> modifyInterestPoint(@NotNull InterestPointDTO modifiedInterestPoint) {

        // (1) 将 DTO 对象转换成 PO 对象
        InterestPointPO po = interestPointPoConverter.convert(modifiedInterestPoint);
        Long id = po.getId();

        // (2) 检查对象是否存在
        if (!interestPointMapper.exists(
                new LambdaQueryWrapper<InterestPointPO>().eq(InterestPointPO::getId,id)
        )) {
            return R.failure(PoiCodeEnum.POI_DATA_NOT_FOUND);
        }

        // (3) 更新数据
        interestPointMapper.updateById(po);

        // (4) 获取更新后的数据并计算其hash
        InterestPointPO updatedPO = interestPointMapper.selectById(id);
        String hash = hash(updatedPO);

        // (5) 检查是否存在 hash
        if (interestPointMapper.exists(
                new LambdaQueryWrapper<InterestPointPO>()
                        .eq(InterestPointPO::getHash,hash)
                        .ne(InterestPointPO::getId,id)
        )) {
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.failure(PoiCodeEnum.DUPLICATE_POI_DATA);
        }

        // (6) 如果hash 不一样，更新 hash
        if (!hash.equals(updatedPO.getHash())) {
            InterestPointPO needUpdateHashPO = new InterestPointPO();
            needUpdateHashPO.setId(id);
            needUpdateHashPO.setHash(hash);
            interestPointMapper.updateById(needUpdateHashPO);
        }

        return R.success();
    }

    /**
     * 根据兴趣点ID删除兴趣点
     * @param id 兴趣点ID
     * @return 无
     */
    @Override
    public R<Void> removeInterestPointById(@NotNull Long id) {

        // (1) 检查对象是否存在
        if (!interestPointMapper.exists(
                new LambdaQueryWrapper<InterestPointPO>().eq(InterestPointPO::getId,id)
        )) {
            return R.failure(PoiCodeEnum.POI_DATA_NOT_FOUND);
        }

        // (2) 删除数据
        interestPointMapper.deleteById(id);

        return R.success();
    }

    /**
     * 根据兴趣点PO对象计算Hash值
     * @param po 兴趣点PO对象
     * @return Hash值字符串
     */
    private String hash(InterestPointPO po) {
        // 明文为：兴趣点名称 + 兴趣点地址
        String plainText = po.getName() + po.getAddress();
        // Hash 为：MD5(明文)
        return DigestUtils.md5DigestAsHex(plainText.getBytes());
    }

}
