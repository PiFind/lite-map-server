package io.pifind.mapserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.mapserver.converter.vo.InterestPointVoConverter;
import io.pifind.mapserver.converter.po.InterestPointPoConverter;
import io.pifind.mapserver.error.PoiCodeEnum;
import io.pifind.mapserver.mapper.InterestPointMapper;
import io.pifind.mapserver.middleware.redis.service.InterestPointSocialRedisService;
import io.pifind.mapserver.model.po.InterestPointPO;
import io.pifind.mapserver.mp.page.MybatisPage;
import io.pifind.mapserver.util.InterestPointHashUtils;
import io.pifind.poi.api.InterestPointPublisherService;
import io.pifind.poi.model.dto.InterestPointDTO;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static io.pifind.mapserver.model.constant.InterestPointStatusEnum.UNVERIFIED;

/**
 * 兴趣点基础服务实现类
 * @see io.pifind.poi.api.InterestPointPublisherService
 */
@Service
public class InterestPointPublisherServiceImpl implements InterestPointPublisherService {

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
    public R<Void> addInterestPoint(@NotEmpty String username,@NotNull InterestPointDTO interestPoint) {

        // (1) 将 DTO 对象转换成 PO 对象
        InterestPointPO po = interestPointPoConverter.convert(interestPoint);
        po.setPublisher(username);

        // (2) 根据PO对象计算 Hash，并设置到 PO 对象中
        String hash = InterestPointHashUtils.hash(
                po.getName(),po.getAddress()
        );
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
     * 这个是给发布者自己查询使用的，所以兴趣点无论是否审核通过都会进行展示
     * @param id 兴趣点ID
     * @return {@link InterestPointVO 兴趣点实体对象}
     */
    @Override
    public R<InterestPointVO> getInterestPointById(@NotEmpty String username,@NotNull Long id) {

        InterestPointPO po = interestPointMapper.selectById(id);

        if (po == null || !po.getPublisher().equals(username)) {
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
     * 根据发布者获取兴趣点分页
     * @param username 用户名
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return {@link Page<InterestPointVO>} 兴趣点分页对象
     */
    @Override
    public R<Page<InterestPointVO>> getInterestPointPageByPublisher(
            @NotEmpty String username,
            @NotNull Integer currentPage,
            @NotNull Integer pageSize
    ) {
        // (1) 获取分页对象
        MybatisPage<InterestPointPO> mybatisPage =
                new MybatisPage<>(currentPage,pageSize);

        // (2) 获取分页数据
        mybatisPage = interestPointMapper.selectPage(
                mybatisPage,
                new LambdaQueryWrapper<InterestPointPO>()
                        .eq(InterestPointPO::getPublisher,username)
        );

        // (3) 将 PO 对象转换成 VO 对象
        List<InterestPointPO> records = mybatisPage.getRecords();
        return R.page(
                (int) mybatisPage.getCurrent(),
                (int) mybatisPage.getSize(),
                (int) mybatisPage.getTotal(),
                interestPointVoConverter.convert(records)
        );
    }

    /**
     * 根据兴趣点ID修改兴趣点
     * @param modifiedInterestPoint 修改过兴趣点信息后的{@link InterestPointDTO 兴趣点实体对象}
     * @return 无
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R<Void> modifyInterestPoint(@NotEmpty String username,@NotNull InterestPointDTO modifiedInterestPoint) {

        // (1) 将 DTO 对象转换成 PO 对象
        InterestPointPO po = interestPointPoConverter.convert(modifiedInterestPoint);
        po.setPublisher(username);
        Long id = po.getId();

        // (2) 检查对象是否存在
        InterestPointPO interestPointPO = interestPointMapper.selectById(id);
        if (interestPointPO == null || !interestPointPO.getPublisher().equals(username) ) {
            return R.failure(PoiCodeEnum.POI_DATA_NOT_FOUND);
        }

        //更新为未审核
        po.setPoiStatus(UNVERIFIED);
        // (3) 更新数据
        interestPointMapper.updateById(po);

        // (4) 获取更新后的数据并计算其hash
        InterestPointPO updatedPO = interestPointMapper.selectById(id);
        String hash = InterestPointHashUtils.hash(
                updatedPO.getName(),updatedPO.getAddress()
        );

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
    public R<Void> removeInterestPointById(@NotEmpty String username,@NotNull Long id) {

        // (1) 检查对象是否存在，且执行删除的用户是否为发布者
        InterestPointPO interestPointPO = interestPointMapper.selectById(id);
        if (interestPointPO == null || !interestPointPO.getPublisher().equals(username) ) {
            return R.failure(PoiCodeEnum.POI_DATA_NOT_FOUND);
        }

        // (2) 删除数据
        interestPointMapper.deleteById(id);

        return R.success();
    }


}
