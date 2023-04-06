package io.pifind.mapserver.service.impl;

import io.pifind.common.response.R;
import io.pifind.mapserver.converter.po.CategoryPoConverter;
import io.pifind.mapserver.converter.vo.CategoryVoConverter;
import io.pifind.mapserver.mapper.CategoryMapper;
import io.pifind.mapserver.model.po.CategoryPO;
import io.pifind.poi.api.ICategoryService;
import io.pifind.poi.model.dto.CategoryEditDTO;
import io.pifind.poi.model.dto.LocalizedNameDTO;
import io.pifind.poi.model.dto.LocalizedNameGroupDTO;
import io.pifind.poi.model.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryPoConverter categoryPoConverter;

    @Autowired
    private CategoryVoConverter categoryVoConverter;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public R<Void> addCategory(@NotNull CategoryEditDTO category) {

        // (1) 从数据库检查上级是否存在
        Long superiorId = category.getSuperior();
        CategoryPO superiorCategory = categoryMapper.selectById(superiorId);
        if (superiorCategory == null) { // 如果不存在就报错
            return R.failure();
        }

        // (2) 将编辑对象转换成持久化对象
        CategoryPO categoryPO = categoryPoConverter.convert(category);
        if (category.getName() != null) {
            Map<Locale,String> localeNameMap = new HashMap<>();
            localeNameMap.put(Locale.getDefault(),category.getName());
            categoryPO.setNames(localeNameMap);
        }
        categoryPO.setLevel(superiorCategory.getLevel() + 1);

        // (3) 加入数据库
        categoryMapper.insert(categoryPO);

        return R.success();
    }

    @Override
    public R<CategoryVO> getCategoryById(@NotNull Long id) {

        // (1) 从数据库中查找分类和子分类
        CategoryPO categoryPO = categoryMapper.selectById(id);
        if (categoryPO == null) {
            return R.failure();
        }
        List<CategoryPO> subcategoryList =  categoryMapper.selectSubcategoryListById(id);

        // (2) 转换数据
        CategoryVO categoryVO = categoryVoConverter.convert(categoryPO);
        List<CategoryVO> subcategoryVoList = categoryVoConverter.convert(subcategoryList);
        categoryVO.setCategories(subcategoryVoList);

        return R.success(categoryVO);

    }

    @Override
    public R<Void> modifyCategory(@NotNull CategoryEditDTO modifiedCategory) {

        // (1) 从数据库中找到当前的分类
        CategoryPO categoryPO = categoryMapper.selectById(modifiedCategory.getId());
        if (categoryPO == null) {
            return R.failure();
        }

        // (2) 转换为要修改的持久化对象
        CategoryPO modifiedCategoryPO = categoryPoConverter.convert(modifiedCategory);
        // 加入本地语言的命名
        Map<Locale,String> localeNameMap = categoryPO.getNames();
        localeNameMap.put(LocaleContextHolder.getLocale(),modifiedCategory.getName());
        modifiedCategoryPO.setNames(localeNameMap);

        // (3) 更新数据库
        categoryMapper.updateById(modifiedCategoryPO);

        return R.success();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> removeCategoryById(@NotNull Long id) {

        // 从数据库中找到当前的分类
        CategoryPO categoryPO = categoryMapper.selectById(id);
        if (categoryPO != null) { // 如果存在分类就进行递归删除

            // 递归删除数据
            recurrenceRemove(
                    categoryMapper.selectSubcategoryListById(id)
            );

            // 删除当前数据
            if (id != 0L) {
                categoryMapper.deleteById(id);
            }

        }

        return R.success();
    }

    /**
     * 递归删除
     * @param categoryList 需要递归删除的分类列表
     */
    private void recurrenceRemove(List<CategoryPO> categoryList) {

        // (1) 如果为空则返回
        if (categoryList.isEmpty()) {
            return;
        }

        // (2) 获取子分类并删除
        for (CategoryPO category : categoryList) {
            // 获取子分类
            List<CategoryPO> subcategoryList =
                    categoryMapper.selectSubcategoryListById(category.getId());
            recurrenceRemove(subcategoryList);
            // 删除子分类
            categoryMapper.deleteById(category.getId());
        }

    }

    @Override
    public R<Void> addLocalizedName(LocalizedNameDTO name) {

        // (1) 从数据库中找到当前的分类
        CategoryPO categoryPO = categoryMapper.selectById(name.getId());
        if (categoryPO == null) {
            return R.failure();
        }
        Map<Locale,String> localeNameMap = categoryPO.getNames();

        // (2) 更新语言
        CategoryPO modifiedCategory = new CategoryPO();
        modifiedCategory.setId(categoryPO.getId());
        localeNameMap.put(
                Locale.forLanguageTag(name.getLanguage()),name.getName()
        );
        modifiedCategory.setNames(localeNameMap);
        categoryMapper.updateById(categoryPO);

        return R.success();

    }

    @Override
    public R<Void> addLocalizedNameGroup(LocalizedNameGroupDTO nameGroup) {

        // (1) 从数据库中找到当前的分类
        CategoryPO categoryPO = categoryMapper.selectById(nameGroup.getId());
        if (categoryPO == null) {
            return R.failure();
        }
        Map<Locale,String> localeNameMap = categoryPO.getNames();

        // (2) 更新语言
        CategoryPO modifiedCategory = new CategoryPO();
        modifiedCategory.setId(categoryPO.getId());
        Map<String,String> names = nameGroup.getNames();
        for (String language : nameGroup.getNames().keySet()) {
            localeNameMap.put(
                    Locale.forLanguageTag(language),names.get(language)
            );
        }
        modifiedCategory.setNames(localeNameMap);
        categoryMapper.updateById(categoryPO);

        return R.success();
    }

    @Override
    public R<Void> modifyLocalizedName(LocalizedNameDTO name) {
        /*
         * 二者功能一致
         */
        return addLocalizedName(name);
    }

    @Override
    public R<Void> removeLocalizedName(Long id, String language) {

        // (1) 从数据库中找到当前的分类
        CategoryPO categoryPO = categoryMapper.selectById(id);
        if (categoryPO == null) {
            return R.failure();
        }
        Map<Locale,String> localeNameMap = categoryPO.getNames();

        // (2) 删除语言
        CategoryPO modifiedCategory = new CategoryPO();
        modifiedCategory.setId(categoryPO.getId());
        localeNameMap.remove(Locale.forLanguageTag(language));
        modifiedCategory.setNames(localeNameMap);
        categoryMapper.updateById(categoryPO);

        return R.success();
    }

    @Override
    public R<Void> removeAllLocalizedNames(Long id) {
        // (1) 从数据库中找到当前的分类
        CategoryPO categoryPO = categoryMapper.selectById(id);
        if (categoryPO == null) {
            return R.failure();
        }

        // (2) 删除所有语言
        CategoryPO modifiedCategory = new CategoryPO();
        modifiedCategory.setId(categoryPO.getId());
        modifiedCategory.setNames(new HashMap<>());
        categoryMapper.updateById(categoryPO);

        return R.success();
    }

}
