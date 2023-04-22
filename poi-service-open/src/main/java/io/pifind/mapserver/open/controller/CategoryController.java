package io.pifind.mapserver.open.controller;

import io.pifind.common.response.R;
import io.pifind.mapserver.open.service.ICategoryFeignService;
import io.pifind.poi.model.vo.CategoryVO;
import io.pifind.role.annotation.RequestPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类别控制器
 */
@RestController
@RequestMapping("/v1/poi/category")
public class CategoryController {

    @Autowired
    private ICategoryFeignService categoryService;

    /**
     * 根据类别ID获取类别实体对象
     * @param id 类别ID
     * @return 返回值类型为 {@link CategoryVO}
     * <ul>
     *     <li><b>存在类别</b> - 返回 {@link CategoryVO 类别实体对象} </li>
     *     <li><b>不存在类别</b> - 返回 {@code null} </li>
     * </ul>
     */
    @GetMapping("/get/{id}")
    @RequestPermission(name = "poi.category.get",description = "获取POI类别")
    public R<CategoryVO> getCategoryById(@PathVariable("id") Long id) {
        return categoryService.getCategoryById(id);
    }

    /**
     * 根据分类等级获取类别实体对象
     * @param level 分类等级
     * @return 返回值类型为 {@link List<CategoryVO>}
     * <ul>
     *     <li><b>存在分类等级</b> - 返回 {@link List<CategoryVO> 类别实体对象列表} </li>
     *     <li><b>不存在类别</b> - 返回 {@code null} </li>
     * </ul>
     */
    @GetMapping("/get/level/{level}")
    @RequestPermission(name = "poi.category.getCategoryListByLevel",description = "根据分类等级获取POI类别")
    public R<List<CategoryVO>> getCategoryListByLevel(@PathVariable("level") Integer level) {
        return categoryService.getCategoryListByLevel(level);
    }

}