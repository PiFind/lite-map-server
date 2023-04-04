package io.pifind.mapserver.service.impl;

import io.pifind.common.response.R;
import io.pifind.mapserver.mapper.CategoryMapper;
import io.pifind.poi.api.ICategoryService;
import io.pifind.poi.model.dto.CategoryDTO;
import io.pifind.poi.model.dto.LocalizedNameDTO;
import io.pifind.poi.model.dto.LocalizedNameGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public R<Void> addCategory(@NotNull CategoryDTO category) {


        return null;
    }

    @Override
    public R<CategoryDTO> getCategoryById(@NotNull Long id) {


        return null;
    }

    @Override
    public R<Void> modifyCategory(@NotNull CategoryDTO modifiedCategory) {


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
