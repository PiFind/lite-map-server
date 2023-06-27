package io.pifind.mapserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.mapserver.converter.vo.InterestPointVoConverter;
import io.pifind.mapserver.mapper.InterestPointCollectMapper;
import io.pifind.mapserver.mapper.InterestPointMapper;
import io.pifind.mapserver.model.po.InterestPointCollectPO;
import io.pifind.mapserver.model.po.InterestPointPO;
import io.pifind.mapserver.mp.page.MybatisPage;
import io.pifind.poi.api.InterestPointCollectService;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * InterestPointCollectServiceImpl
 *
 * @author chenxiaoli
 * @date 2023-06-27
 * @description 收藏服务
 */
@Service
public class InterestPointCollectServiceImpl implements InterestPointCollectService {

    @Autowired
    private InterestPointCollectMapper interestPointCollectMapper;

    @Autowired
    private InterestPointMapper interestPointMapper;

    @Autowired
    private InterestPointVoConverter interestPointVoConverter;

    @Override
    public R<Void> collect(String username, Long interestPointId) {
        InterestPointCollectPO pointCollectPO = new InterestPointCollectPO();
        pointCollectPO.setUsername(username);
        pointCollectPO.setInterestPointId(interestPointId);
        List<InterestPointCollectPO> pointCollects = interestPointCollectMapper.selectList(
                new LambdaQueryWrapper<InterestPointCollectPO>()
                .eq(InterestPointCollectPO::getUsername, username)
                .eq(InterestPointCollectPO::getInterestPointId, interestPointId));
        if (!CollectionUtils.isEmpty(pointCollects)) {
            return R.success();
        }
        interestPointCollectMapper.insert(pointCollectPO);
        return R.success();
    }

    @Override
    public R<Void> cancel(String username, Long interestPointId) {
        interestPointCollectMapper.delete(new LambdaQueryWrapper<InterestPointCollectPO>()
                .eq(InterestPointCollectPO::getUsername, username)
                .eq(InterestPointCollectPO::getInterestPointId, interestPointId));
        return R.success();
    }

    @Override
    public R<Page<InterestPointVO>> getCollectList(String username, Integer currentPage, Integer pageSize) {
        MybatisPage<InterestPointCollectPO> collectPage = new MybatisPage<>(currentPage,pageSize);
        collectPage.addOrder(OrderItem.desc("create_time"));

        interestPointCollectMapper.selectPage(collectPage, new LambdaQueryWrapper<InterestPointCollectPO>()
                        .eq(InterestPointCollectPO::getUsername, username));

        List<InterestPointCollectPO> records = collectPage.getRecords();
        List<InterestPointVO> pointVOList = new ArrayList<>();
        for (InterestPointCollectPO collect : records) {
            InterestPointPO interestPointPO = interestPointMapper.selectById(collect.getInterestPointId());
            InterestPointVO interestPointVO = interestPointVoConverter.convert(interestPointPO);
            pointVOList.add(interestPointVO);
        }

        return R.page((int) collectPage.getCurrent(), (int) collectPage.getSize(), (int) collectPage.getTotal(), pointVOList);
    }
}
