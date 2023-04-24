package io.pifind.mapserver.open.service;

import io.pifind.common.response.R;
import io.pifind.mapserver.open.support.PoiServiceAPI;
import io.pifind.poi.model.vo.CategoryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 公司标签服务
 */
@FeignClient(name = "poi-category-service",url = PoiServiceAPI.CATEGORY_URL)
public interface ICategoryFeignService {

    /**
     * 根据类别ID获取类别实体对象
     * @param id 类别ID
     * @return 返回值类型为 {@link CategoryVO}
     * <ul>
     *     <li><b>存在类别</b> - 返回 {@link CategoryVO 类别实体对象} </li>
     *     <li><b>不存在类别</b> - 返回 {@code null} </li>
     * </ul>
     */
    @GetMapping("/category/get/{id}")
    R<CategoryVO> getCategoryById(
            @PathVariable("id") Long id
    );

    /**
     * 根据分类等级获取类别实体对象
     * @param level 分类等级
     * @return 返回值类型为 {@link List<CategoryVO>}
     * <ul>
     *     <li><b>存在分类等级</b> - 返回 {@link List<CategoryVO> 类别实体对象列表} </li>
     *     <li><b>不存在类别</b> - 返回 {@code null} </li>
     * </ul>
     */
    @GetMapping("/category/get/level/{level}")
    R<List<CategoryVO>> getCategoryListByLevel(
            @PathVariable("level") Integer level
    );

}
