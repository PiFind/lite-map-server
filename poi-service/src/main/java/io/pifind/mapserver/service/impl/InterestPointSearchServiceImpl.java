package io.pifind.mapserver.service.impl;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.poi.api.InterestPointSearchService;
import io.pifind.poi.model.InterestPointDTO;
import io.pifind.poi.model.SortingModeDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

public class InterestPointSearchServiceImpl implements InterestPointSearchService {

    @Override
    public R<Page<InterestPointDTO>> searchPointsByAreaAndKeywords(@NotNull Integer pageSize, @NotNull Integer currentPage, @NotNull Long areaId, @NotNull List<String> keywords, @NotNull SortingModeDTO sortingMode) {
        return null;
    }

    @Override
    public R<Page<InterestPointDTO>> searchPointsByAreaAndCategory(@NotNull Integer pageSize, @NotNull Integer currentPage, @NotNull Long areaId, @NotNull Long categoryId, @NotNull SortingModeDTO sortingMode) {
        return null;
    }

    @Override
    public R<Page<InterestPointDTO>> searchPointsByAreaAndCategoryAndKeywords(@NotNull Integer pageSize, @NotNull Integer currentPage, @NotNull Long areaId, @NotNull Long categoryId, @NotNull List<String> keywords, @NotNull SortingModeDTO sortingMode) {
        return null;
    }

    @Override
    public R<Page<InterestPointDTO>> searchNearbyPoints(@NotNull Integer pageSize, @NotNull Integer currentPage, @NotNull CoordinateDTO coordinate, @NotNull Integer range, @NotNull Long categoryId, @NotNull SortingModeDTO sortingMode) {
        return null;
    }

}
