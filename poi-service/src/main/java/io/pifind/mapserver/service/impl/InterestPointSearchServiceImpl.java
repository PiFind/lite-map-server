package io.pifind.mapserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.mapserver.converter.dto.InterestPointVoConverter;
import io.pifind.mapserver.converter.po.InterestPointPoConverter;
import io.pifind.mapserver.mapper.InterestPointMapper;
import io.pifind.mapserver.model.po.InterestPointPO;
import io.pifind.mapserver.mp.page.MybatisPage;
import io.pifind.place.api.IAdministrativeAreaService;
import io.pifind.place.model.AdministrativeAreaDTO;
import io.pifind.poi.api.InterestPointSearchService;
import io.pifind.poi.constant.SortOrderEnum;
import io.pifind.poi.constant.SortReferenceEnum;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class InterestPointSearchServiceImpl implements InterestPointSearchService {

    @Autowired
    private InterestPointVoConverter interestPointVoConverter;

    @Autowired
    private InterestPointPoConverter interestPointPoConverter;


    @Autowired
    private InterestPointMapper interestPointMapper;

    @Autowired
    private IAdministrativeAreaService administrativeAreaService;

    @Override
    public R<Page<InterestPointVO>> searchPoints(
            @NotNull  Integer pageSize,
            @NotNull  Integer currentPage,
            @NotNull  Long areaId,
            @Nullable Long categoryId,
            @Nullable String keyword,
            @NotNull  SortOrderEnum sortOrder,
            @NotNull  SortReferenceEnum reference
    ) {

        // +------------+
        //  构建查询包装器
        // +------------+

        LambdaQueryWrapper<InterestPointPO> queryWrapper =
                new LambdaQueryWrapper<InterestPointPO>();
        // (1) 加入查找行政区的条件
        queryWrapper = findAreasQueryWrapper(queryWrapper,areaId);
        // (2) 加入查找分类的条件
        if (categoryId != null) {
            queryWrapper = queryWrapper.eq(InterestPointPO::getCategoryId,categoryId);
        }
        // (3) 加入关键词的条件
        if (keyword != null) {
            queryWrapper = keywordQueryWrapper(queryWrapper,keyword);
        }
        // (4) 加入可信度条件(可信度需要大于等于60分)
        queryWrapper = queryWrapper.ge(InterestPointPO::getReliability,60);
        // (5) 加入排序的条件
        queryWrapper = sortQueryWrapper(queryWrapper,sortOrder,reference);

        // +------------------+
        //  构建原始分页并查找数据
        // +------------------+

        MybatisPage<InterestPointPO> interestPointPage = new MybatisPage<>(
                currentPage,
                pageSize
        );

        // 查找数据
        interestPointPage = interestPointMapper.selectPage(
                interestPointPage, queryWrapper
        );

        // 获取记录转换成标准返回页
        List<InterestPointPO> records = interestPointPage.getRecords();
        return R.page(
                (int) interestPointPage.getCurrent(),
                (int) interestPointPage.getSize(),
                (int) interestPointPage.getTotal(),
                interestPointVoConverter.convert(records)
        );
    }

    /**
     * 将查找关键词的条件加入到查找包装器
     * @param queryWrapper 查找包装器
     * @param keyword 管家词
     * @return 查找包装器
     */
    private LambdaQueryWrapper<InterestPointPO> keywordQueryWrapper (
            LambdaQueryWrapper<InterestPointPO> queryWrapper,
            String keyword
    ) {
        if (keyword.trim().isEmpty()) {
            String realKeyword = keyword.trim();
            queryWrapper = queryWrapper.and(wrapper -> {
                wrapper
                    .like(InterestPointPO::getName,realKeyword)
                    .or()
                    .like(InterestPointPO::getNameEN,realKeyword);
            });
        }
        return queryWrapper;
    }

    /**
     * 将查找行政区的条件加入查找包装器
     * @param queryWrapper 查找包装器
     * @param areaId 行政区ID
     * @return 查找包装器
     */
    private LambdaQueryWrapper<InterestPointPO> findAreasQueryWrapper (
            LambdaQueryWrapper<InterestPointPO> queryWrapper,
            Long areaId
    ) {
        // 获取 areaId 下面的行政区 ID ,最多遍历两个层级
        List<Long> areaIdList = new ArrayList<>();
        areaIdList.add(areaId);
        R<AdministrativeAreaDTO> areaInfo = administrativeAreaService.getAdministrativeAreaById(
                areaId,2
        );
        List<AdministrativeAreaDTO> subAreas = areaInfo.getData().getAreas();
        for (AdministrativeAreaDTO subArea : subAreas) {
            areaIdList.add(subArea.getId());
            if (!subArea.getAreas().isEmpty()) {
                for (AdministrativeAreaDTO minSubArea : subArea.getAreas()) {
                    areaIdList.add(minSubArea.getId());
                }
            }
        }
        queryWrapper = queryWrapper.in(InterestPointPO::getAdministrativeAreaId,areaIdList);
        return queryWrapper;
    }

    /**
     * 将排序条件加入查找包装器
     * @param queryWrapper 查找包装器
     * @param sortOrder 排序
     * @param reference 引用
     * @return 查找包装器
     */
    private LambdaQueryWrapper<InterestPointPO> sortQueryWrapper (
            LambdaQueryWrapper<InterestPointPO> queryWrapper,
            SortOrderEnum sortOrder,
            SortReferenceEnum reference
    ) {
        switch (sortOrder) {
            case ASCENDING: {
                switch (reference) {
                    case SCORE:
                        queryWrapper = queryWrapper.orderByAsc(InterestPointPO::getTotalScore);
                        break;
                    case PAGEVIEWS:
                        queryWrapper = queryWrapper.orderByAsc(InterestPointPO::getPageviews);
                        break;
                    case COLLECTIONS:
                        queryWrapper = queryWrapper.orderByAsc(InterestPointPO::getCollections);
                        break;
                }
            }
            case DESCENDING: {
                switch (reference) {
                    case SCORE:
                        queryWrapper = queryWrapper.orderByDesc(InterestPointPO::getTotalScore);
                        break;
                    case PAGEVIEWS:
                        queryWrapper = queryWrapper.orderByDesc(InterestPointPO::getPageviews);
                        break;
                    case COLLECTIONS:
                        queryWrapper = queryWrapper.orderByDesc(InterestPointPO::getCollections);
                        break;
                }
            }
        }
        return queryWrapper;
    }


}
