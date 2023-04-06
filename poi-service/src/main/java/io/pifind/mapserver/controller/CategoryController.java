package io.pifind.mapserver.controller;

import io.pifind.common.response.R;
import io.pifind.poi.api.ICategoryService;
import io.pifind.poi.model.dto.CategoryEditDTO;
import io.pifind.poi.model.dto.LocalizedNameDTO;
import io.pifind.poi.model.dto.LocalizedNameGroupDTO;
import io.pifind.poi.model.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 类别控制器
 */
@RestController
@RequestMapping("/v1/poi/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 添加一个类别
     * @param category {@link CategoryEditDTO 类别实体对象}
     * @return 无返回值
     * <ul>
     *     <li><b>添加类别成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>添加类别失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @PostMapping("/add")
    public R<Void> addCategory(@RequestBody CategoryEditDTO category) {
        return categoryService.addCategory(category);
    }

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
    public R<CategoryVO> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    /**
     * 修改类别信息
     * @param modifiedCategory 修改过后的{@link CategoryEditDTO 类别实体对象}
     * @return 无返回值
     * <ul>
     *     <li><b>修改类别成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>修改类别失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @PostMapping("/modify")
    public R<Void> modifyCategory(@RequestBody CategoryEditDTO modifiedCategory) {
        return categoryService.modifyCategory(modifiedCategory);
    }

    /**
     * 通过类别ID删除类别
     * <p>
     *     <font style="color:red;">
     *     <b>注意：</b> 如果删除的类别下有子类别，那么也会删除其子类别
     *     </font>
     * </p>
     * @param id 类别ID
     * @return 无返回值
     * <ul>
     *     <li><b>删除类别成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>删除类别失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @DeleteMapping("/remove/{id}")
    public R<Void> removeCategoryById(@PathVariable Long id) {
        return categoryService.removeCategoryById(id);
    }

    /**
     * 添加本地化命名
     * @param name {@link LocalizedNameDTO 本地化命名实体对象 }
     * @return 无返回值
     * <ul>
     *     <li><b>添加本地化命名成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>添加本地化命名失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @PostMapping("/addLocalizedName")
    public R<Void> addLocalizedName(@RequestBody LocalizedNameDTO name) {
        return categoryService.addLocalizedName(name);
    }

    /**
     * 添加本地化命名组
     * @param nameGroup {@link LocalizedNameGroupDTO 本地化命名组实体对象 }
     * @return 无返回值
     * <ul>
     *     <li><b>添加本地化命名组成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>添加本地化命名组失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @PostMapping("/addLocalizedNameGroup")
    public R<Void> addLocalizedNameGroup(@RequestBody LocalizedNameGroupDTO nameGroup) {
        return categoryService.addLocalizedNameGroup(nameGroup);
    }

    /**
     * 修改本地化命名
     * @param name {@link LocalizedNameDTO 本地化命名实体对象 }
     * @return 无返回值
     * <ul>
     *     <li><b>修改本地化命名成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>修改本地化命名失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @PostMapping("/modifyLocalizedName")
    public R<Void> modifyLocalizedName(@RequestBody LocalizedNameDTO name) {
        return categoryService.modifyLocalizedName(name);
    }

    /**
     * 移除本地化命名
     * @param id 类别ID
     * @param language 语言代码
     * @return 无返回值
     * <ul>
     *     <li><b>移除本地化命名成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>移除本地化命名失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @DeleteMapping("/removeLocalizedName/{id}/{language}")
    public R<Void> removeLocalizedName(
            @PathVariable Long id,
            @PathVariable String language
    ) {
        return categoryService.removeLocalizedName(id,language);
    }

    /**
     * 移除所有本地化命名
     * @param id 类别ID
     * @return 无返回值
     * <ul>
     *     <li><b>移除所有本地化命名成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>移除所有本地化命名失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @DeleteMapping("/removeAllLocalizedNames/{id}")
    public R<Void> removeAllLocalizedNames(@PathVariable Long id) {
        return categoryService.removeAllLocalizedNames(id);
    }

}