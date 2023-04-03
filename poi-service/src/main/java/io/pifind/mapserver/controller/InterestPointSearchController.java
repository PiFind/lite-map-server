package io.pifind.mapserver.controller;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointSearchService;
import io.pifind.poi.constant.SortOrderEnum;
import io.pifind.poi.constant.SortReferenceEnum;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/v1/poi/search")
public class InterestPointSearchController {

    @Autowired
    private InterestPointSearchService interestPointSearchService ;

    @GetMapping("/condition")
    public R<Page<InterestPointVO>> searchPointsByAreaAndCategoryAndKeywords(
            @NotNull Integer pageSize,
            @NotNull Integer currentPage,
            @NotNull Long areaId,
            Long categoryId,
            String keyword,
            @NotNull SortOrderEnum sortOrder,
            @NotNull SortReferenceEnum reference) {
        return interestPointSearchService.searchPoints(
                pageSize,
                currentPage,
                areaId,
                categoryId,
                keyword,
                sortOrder,
                reference
        );
    }

}
