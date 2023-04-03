package io.pifind.mapserver.service.impl;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointSearchService;
import io.pifind.poi.constant.SortOrderEnum;
import io.pifind.poi.constant.SortReferenceEnum;
import io.pifind.poi.model.vo.InterestPointVO;

import javax.validation.constraints.NotNull;

public class InterestPointSearchServiceImpl implements InterestPointSearchService {

    @Override
    public R<Page<InterestPointVO>> searchPointsByAreaAndKeywords(@NotNull Integer pageSize, @NotNull Integer currentPage, @NotNull Long areaId, @NotNull String keyword, @NotNull SortOrderEnum sortOrder, @NotNull SortReferenceEnum reference) {
        return null;
    }

    @Override
    public R<Page<InterestPointVO>> searchPointsByAreaAndCategory(@NotNull Integer pageSize, @NotNull Integer currentPage, @NotNull Long areaId, @NotNull Long categoryId, @NotNull SortOrderEnum sortOrder, @NotNull SortReferenceEnum reference) {
        return null;
    }

    @Override
    public R<Page<InterestPointVO>> searchPointsByAreaAndCategoryAndKeywords(@NotNull Integer pageSize, @NotNull Integer currentPage, @NotNull Long areaId, @NotNull Long categoryId, @NotNull String keyword, @NotNull SortOrderEnum sortOrder, @NotNull SortReferenceEnum reference) {
        return null;
    }

}
