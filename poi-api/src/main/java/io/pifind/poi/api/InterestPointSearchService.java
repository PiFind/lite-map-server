package io.pifind.poi.api;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.place.model.AdministrativeAreaDTO;
import io.pifind.poi.constant.SortOrderEnum;
import io.pifind.poi.constant.SortReferenceEnum;
import io.pifind.poi.model.dto.CategoryEditDTO;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * 兴趣点搜索服务
 * <p>
 *     本类中的方法返回值均为 {@link Page } 类型
 * </p>
 * @see Page
 */
public interface InterestPointSearchService {

    /**
     * 通过兴趣点ID查看兴趣点
     * @param id 兴趣点ID
     * @return 返回值为 {@link InterestPointVO}
     */
    R<InterestPointVO> viewPointById(@NotNull Long id);

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
    R<Page<InterestPointVO>> searchPoints(
            @NotNull  Integer pageSize,
            @NotNull  Integer currentPage,
            @NotNull  Long areaId,
            @Nullable Long categoryId,
            @Nullable String keyword,
            @NotNull  SortOrderEnum sortOrder,
            @NotNull  SortReferenceEnum reference
    );

}