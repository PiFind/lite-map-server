package io.pifind.mapserver.controller;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.place.model.AdministrativeAreaDTO;
import io.pifind.poi.api.InterestPointSearchService;
import io.pifind.poi.constant.SortOrderEnum;
import io.pifind.poi.constant.SortReferenceEnum;
import io.pifind.poi.model.vo.CategoryVO;
import io.pifind.poi.model.vo.InterestPointVO;
import io.pifind.role.annotation.RequestPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 兴趣点搜索控制器
 */
@RestController
@RequestMapping("/v1/poi/search")
public class InterestPointSearchController {

    @Autowired
    private InterestPointSearchService interestPointSearchService ;

    /**
     * 通过ID搜索兴趣点
     * @param id 兴趣点ID
     * @return 兴趣点
     */
    @GetMapping("/view/{id}")
    @RequestPermission(name = "poi.search.id",description = "通过ID搜索兴趣点")
    public R<InterestPointVO> viewPointById(@PathVariable("id") Long id) {
        return interestPointSearchService.viewPointById(id);
    }

    /**
     * 通过地区、类别和关键字搜索兴趣点（模糊搜索）
     * @param pageSize 页大小（一页最多存放多少条数据）
     * @param currentPage 当前页
     * @param areaId 搜索的区域的ID (参考 : {@link AdministrativeAreaDTO})
     * @param categoryId 搜索的类别的ID (参考 : {@link CategoryVO})
     * @param keyword 关键词
     * @param sortOrder 排序模式
     * @param reference 排序参考字段
     * @return {@link Page } ，如果没有搜索到结果 {@link Page#getTotal() } 将为 0
     * @see AdministrativeAreaDTO
     * @see CategoryVO
     */
    @GetMapping("/condition")
    @RequestPermission(name = "poi.search.condition",description = "通过地区、类别和关键字等搜索兴趣点")
    public R<Page<InterestPointVO>> searchPoints(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("areaId") Long areaId,
            @RequestParam(value = "categoryId",required = false) Long categoryId,
            @RequestParam(value = "keyword",required = false) String keyword,
            @RequestParam("sortOrder") SortOrderEnum sortOrder,
            @RequestParam("reference") SortReferenceEnum reference
    ) {
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
