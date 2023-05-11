package io.pifind.mapserver.open.service;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.mapserver.open.support.PoiServiceAPI;
import io.pifind.place.model.AdministrativeAreaDTO;
import io.pifind.poi.constant.SortOrderEnum;
import io.pifind.poi.constant.SortReferenceEnum;
import io.pifind.poi.model.dto.CategoryEditDTO;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 兴趣点搜索服务
 * <p>
 *     本类中的方法返回值均为 {@link Page } 类型
 * </p>
 * @see Page
 */
@FeignClient(name = "poi-search-service",url = PoiServiceAPI.SEARCH_URL)
public interface InterestPointSearchFeignService {

    /**
     * 通过兴趣点ID查看兴趣点
     * @param id 兴趣点ID
     * @return 返回值为 {@link InterestPointVO}
     */
    @GetMapping("/view/{id}")
    R<InterestPointVO> viewPointById(
            @PathVariable("id") Long id
    );

    /**
     * 通过地区、类别和关键字搜索兴趣点（模糊搜索）
     * @param pageSize 页大小（一页最多存放多少条数据）
     * @param currentPage 当前页
     * @param areaId 搜索的区域的ID (参考 : {@link AdministrativeAreaDTO})
     * @param categoryId 搜索的类别的ID (参考 : {@link CategoryEditDTO})
     * @param keyword 关键词
     * @param sortOrder 排序模式
     * @param reference 排序参考字段
     * @return {@link Page } ，如果没有搜索到结果 {@link Page#getTotal() } 将为 0
     * @see AdministrativeAreaDTO
     * @see CategoryEditDTO
     */
    @GetMapping("/condition")
    R<Page<InterestPointVO>> searchPoints(
            @RequestParam("pageSize")    Integer pageSize,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("areaId")      Long areaId,
            @RequestParam(value = "categoryId",required = false) Long categoryId,
            @RequestParam(value = "keyword",required = false) String keyword,
            @RequestParam("sortOrder")   SortOrderEnum sortOrder,
            @RequestParam("reference")   SortReferenceEnum reference
    );

}