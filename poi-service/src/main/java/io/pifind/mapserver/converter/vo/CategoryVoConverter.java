package io.pifind.mapserver.converter.vo;

import io.pifind.common.converter.AdvancedConverter;
import io.pifind.mapserver.model.po.CategoryPO;
import io.pifind.poi.model.vo.CategoryVO;
import org.mapstruct.Mapper;

import java.util.Locale;

@Mapper(componentModel = "spring")
public interface CategoryVoConverter extends AdvancedConverter<CategoryPO, CategoryVO> {

    @Override
    default CategoryVO convert(CategoryPO source) {
        CategoryVO categoryVO = new CategoryVO();
        Locale locale = Locale.CHINA;
        if (source.getNames() != null && source.getNames().containsKey(locale)) {
            categoryVO.setName(source.getNames().get(locale));
        } else {
            categoryVO.setName(source.getNameEN());
        }
        categoryVO.setNameEN(source.getNameEN());
        categoryVO.setId(source.getId());
        categoryVO.setCode(source.getCode());
        categoryVO.setSuperior(source.getSuperior());
        categoryVO.setUpdateTime(source.getUpdateTime());
        categoryVO.setCreateTime(source.getCreateTime());
        return categoryVO;
    }

}
