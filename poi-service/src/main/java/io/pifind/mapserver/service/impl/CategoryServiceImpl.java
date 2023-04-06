package io.pifind.mapserver.service.impl;

import io.pifind.common.response.R;
import io.pifind.mapserver.mapper.CategoryMapper;
import io.pifind.poi.api.ICategoryService;
import io.pifind.poi.model.dto.CategoryEditDTO;
import io.pifind.poi.model.dto.LocalizedNameDTO;
import io.pifind.poi.model.dto.LocalizedNameGroupDTO;
import io.pifind.poi.model.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public R<Void> addCategory(@NotNull CategoryEditDTO category) {
        return null;
    }

    @Override
    public R<CategoryVO> getCategoryById(@NotNull Long id) {
        return null;
    }

    @Override
    public R<Void> modifyCategory(@NotNull CategoryEditDTO modifiedCategory) {
        return null;
    }

    @Override
    public R<Void> removeCategoryById(@NotNull Long id) {
        return null;
    }

    @Override
    public R<Void> addLocalizedName(LocalizedNameDTO name) {
        return null;
    }

    @Override
    public R<Void> addLocalizedNameGroup(LocalizedNameGroupDTO nameGroup) {
        return null;
    }

    @Override
    public R<Void> modifyLocalizedName(LocalizedNameDTO name) {
        return null;
    }

    @Override
    public R<Void> removeLocalizedName(Long id, String language) {
        return null;
    }

    @Override
    public R<Void> removeAllLocalizedNames(Long id) {
        return null;
    }

}
