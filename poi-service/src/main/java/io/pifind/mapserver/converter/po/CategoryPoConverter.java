package io.pifind.mapserver.converter.po;

import io.pifind.mapserver.converter.AdvancedConverter;
import io.pifind.mapserver.model.po.CategoryPO;
import io.pifind.poi.model.dto.CategoryEditDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryPoConverter extends AdvancedConverter<CategoryEditDTO,CategoryPO> {

    @Override
    default CategoryPO convert(CategoryEditDTO source) {
        CategoryPO categoryPO = new CategoryPO();
        categoryPO.setId(source.getId());

        return categoryPO;
    }

}
