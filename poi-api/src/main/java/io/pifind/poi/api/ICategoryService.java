package io.pifind.poi.api;

import io.pifind.common.response.R;
import io.pifind.poi.model.dto.CategoryEditDTO;
import io.pifind.poi.model.dto.LocalizedNameDTO;
import io.pifind.poi.model.dto.LocalizedNameGroupDTO;
import io.pifind.poi.model.vo.CategoryVO;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 公司标签服务
 */
public interface ICategoryService {

    /**
     * 添加一个类别
     * @param category {@link CategoryEditDTO 类别实体对象}
     * @return 无返回值
     * <ul>
     *     <li><b>添加类别成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>添加类别失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    R<Void> addCategory(@NotNull CategoryEditDTO category);

    /**
     * 根据类别ID获取类别实体对象
     * @param id 类别ID
     * @return 返回值类型为 {@link CategoryVO}
     * <ul>
     *     <li><b>存在类别</b> - 返回 {@link CategoryVO 类别实体对象} </li>
     *     <li><b>不存在类别</b> - 返回 {@code null} </li>
     * </ul>
     */
    R<CategoryVO> getCategoryById(@NotNull Long id);

    /**
     * 根据分类等级获取类别实体对象
     * @param level 分类等级
     * @return 返回值类型为 {@link List<CategoryVO>}
     * <ul>
     *     <li><b>存在分类等级</b> - 返回 {@link List<CategoryVO> 类别实体对象列表} </li>
     *     <li><b>不存在类别</b> - 返回 {@code null} </li>
     * </ul>
     */
    R<List<CategoryVO>> getCategoryListByLevel(@NotNull Integer level);

    /**
     * 修改类别信息
     * @param modifiedCategory 修改过后的{@link CategoryEditDTO 类别实体对象}
     * @return 无返回值
     * <ul>
     *     <li><b>修改类别成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>修改类别失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    R<Void> modifyCategory(@NotNull CategoryEditDTO modifiedCategory);

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
    R<Void> removeCategoryById(@NotNull Long id);

    /**
     * 添加本地化命名
     * @param name {@link LocalizedNameDTO 本地化命名实体对象 }
     * @return 无返回值
     * <ul>
     *     <li><b>添加本地化命名成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>添加本地化命名失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    R<Void> addLocalizedName(LocalizedNameDTO name);

    /**
     * 添加本地化命名组
     * @param nameGroup {@link LocalizedNameGroupDTO 本地化命名组实体对象 }
     * @return 无返回值
     * <ul>
     *     <li><b>添加本地化命名组成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>添加本地化命名组失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    R<Void> addLocalizedNameGroup(LocalizedNameGroupDTO nameGroup);

    /**
     * 修改本地化命名
     * @param name {@link LocalizedNameDTO 本地化命名实体对象 }
     * @return 无返回值
     * <ul>
     *     <li><b>修改本地化命名成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>修改本地化命名失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    R<Void> modifyLocalizedName(LocalizedNameDTO name);

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
    R<Void> removeLocalizedName(Long id,String language);

    /**
     * 移除所有本地化命名
     * @param id 类别ID
     * @return 无返回值
     * <ul>
     *     <li><b>移除所有本地化命名成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>移除所有本地化命名失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    R<Void> removeAllLocalizedNames(Long id);

}
