package io.pifind.mapserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.mapserver.converter.vo.InterestPointVoConverter;
import io.pifind.mapserver.error.PoiCodeEnum;
import io.pifind.mapserver.mapper.InterestPointMapper;
import io.pifind.mapserver.mapper.InterestPointReviewMapper;
import io.pifind.mapserver.middleware.redis.service.IUserVoteService;
import io.pifind.mapserver.middleware.redis.model.UserVoteRecordDTO;
import io.pifind.mapserver.model.constant.InterestPointStatusEnum;
import io.pifind.mapserver.model.po.InterestPointPO;
import io.pifind.mapserver.model.po.InterestPointReviewPO;
import io.pifind.mapserver.mp.page.MybatisPage;
import io.pifind.poi.api.InterestPointDaoService;
import io.pifind.poi.model.dto.DaoVoteDTO;
import io.pifind.poi.model.vo.InterestPointReviewVO;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.pifind.mapserver.model.constant.InterestPointStatusEnum.VERIFIED;

/**
 * 兴趣点基础服务实现类
 */
@Service
public class InterestPointDaoServiceImpl implements InterestPointDaoService {

    public static final int PASS_VOTE_COUNT = 5;

    public static final int MAX_VOTE_COUNT = 10;

    @Autowired
    private InterestPointVoConverter interestPointVoConverter;

    @Autowired
    private IUserVoteService userVoteService;

    @Autowired
    private InterestPointMapper interestPointMapper;

    @Autowired
    private InterestPointReviewMapper interestPointReviewMapper;

    /**
     * 投票
     * @param username 投票者
     * @param voteDTO 投票信息
     * @return 投票结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> vote(String username, DaoVoteDTO voteDTO) {

        // (1) 检查用户是否已经投过票了
        Boolean hasVoted = userVoteService.hasVoted(username, voteDTO.getInterestPointId());
        if (hasVoted) {
            return R.failure();
        }

        // (2) 获取兴趣点信息是否存在
        InterestPointPO interestPointPO = interestPointMapper.selectById(voteDTO.getInterestPointId());
        if (interestPointPO == null) {
            return R.failure(PoiCodeEnum.POI_DATA_NOT_FOUND);
        }

        // (3) 判断兴趣点是否是自己
        if (username.equals(interestPointPO.getPublisher())) {
            return R.failure(PoiCodeEnum.POI_DATA_OWNER);
        }

        // (4) 判断兴趣点是否已审核过
        if (Objects.nonNull(interestPointPO.getPoiStatus()) && VERIFIED.code() == interestPointPO.getPoiStatus().code()) {
            return R.failure(PoiCodeEnum.POI_DATA_OWNER);
        }

        // (5) 对兴趣点进行投票
        UserVoteRecordDTO userVoteRecord = userVoteService.vote(username, voteDTO.getInterestPointId(), voteDTO.getAgree());

        // (6) 如果兴趣点的投票同意通过大于等于 PASS_VOTE_COUNT 则将兴趣点的状态改为已审核
        if (userVoteRecord.getAgrees() > PASS_VOTE_COUNT) {

            // 更新数据
            InterestPointPO modifiedInterestPointPO = new InterestPointPO();
            modifiedInterestPointPO.setId(voteDTO.getInterestPointId());
            modifiedInterestPointPO.setPoiStatus(VERIFIED);
            interestPointMapper.updateById(modifiedInterestPointPO);

            // 删除数据
            userVoteService.remove(voteDTO.getInterestPointId());
        }

        // (7) 增加一个投票记录
        InterestPointReviewPO interestPointReviewPO = new InterestPointReviewPO();
        interestPointReviewPO.setUsername(username);
        interestPointReviewPO.setInterestPointId(voteDTO.getInterestPointId());
        interestPointReviewPO.setAgree(voteDTO.getAgree());
        interestPointReviewMapper.insert(interestPointReviewPO);
        return R.success();
    }

    /**
     * 是否已经投过票
     * @param username 投票者
     * @param interestPointId 兴趣点ID
     * @return 是否已经投过票
     */
    @Override
    public R<Boolean> hasVoted(String username, Long interestPointId) {
        // 检查用户是否已经投过票了
        return R.success(userVoteService.hasVoted(username, interestPointId));
    }

    /**
     * 获取待审核列表
     * @param username 用户名
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 待审核列表
     */
    @Override
    public R<Page<InterestPointVO>> getReviewPage(String username, Integer currentPage, Integer pageSize) {
        // (1) 获取待审核列表
        MybatisPage<InterestPointPO> page = new MybatisPage<>(currentPage, pageSize);
        page = (MybatisPage<InterestPointPO>)
                interestPointMapper.selectReviewInterestPointPage(page, username);

        // (2) 转换结果
        List<InterestPointVO> records ;
        if (page.getRecords() != null) {
            records = interestPointVoConverter.convert(page.getRecords());
        } else {
            records = new ArrayList<>();
        }

        // (3) 返回待审核列表
        return R.page(
                (int) page.getCurrent(),
                (int) page.getSize(),
                (int) page.getTotal(),
                records
        );
    }

    @Override
    public R<List<InterestPointReviewVO>> getReviewList(Long interestPointId) {
        List<InterestPointReviewPO> reviewList = interestPointReviewMapper.getReviewList(interestPointId);
        if (CollectionUtils.isEmpty(reviewList)) {
            return R.success();
        }
        List<InterestPointReviewVO> reviewVOList = reviewList.stream().map((review) -> {
            InterestPointReviewVO reviewVO = new InterestPointReviewVO();
            BeanUtils.copyProperties(review, reviewVO);
            return reviewVO;
        }).collect(Collectors.toList());
        return R.success(reviewVOList);
    }

    @Override
    public R<Page<InterestPointReviewVO>> getReviewList(String username, Integer currentPage, Integer pageSize) {
        MybatisPage<InterestPointReviewPO> reviewPage = new MybatisPage<>(currentPage, pageSize);
        interestPointReviewMapper.selectPage(reviewPage, new LambdaQueryWrapper<InterestPointReviewPO>()
                .eq(InterestPointReviewPO::getUsername, username)
                .orderByDesc(InterestPointReviewPO::getUpdateTime)
        );
        List<InterestPointReviewVO> records;
        if (CollectionUtils.isEmpty(reviewPage.getRecords())) {
            records = new ArrayList<>();
        } else {
            records = reviewPage.getRecords().stream().map((review) -> {
                InterestPointPO interestPointPO = interestPointMapper.selectById(review.getInterestPointId());
                InterestPointVO interestPointVO = new InterestPointVO();
                InterestPointReviewVO reviewVO = new InterestPointReviewVO();
                BeanUtils.copyProperties(review, reviewVO);
                BeanUtils.copyProperties(interestPointPO, interestPointVO);
                reviewVO.setInterestPoint(interestPointVO);
                return reviewVO;
            }).collect(Collectors.toList());
        }
        return R.page(
                (int) reviewPage.getCurrent(),
                (int) reviewPage.getSize(),
                (int) reviewPage.getTotal(),
                records
        );
    }


}
