package io.pifind.mapserver.converter;

import io.pifind.mapserver.model.AdministrativeAreaPO;
import io.pifind.place.model.AdministrativeAreaDTO;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * {@link AdministrativeAreaDTO} 实体类转换器
 * <p>支持转换的对象如下：</p>
 * <ul>
 *     <li>{@link AdministrativeAreaPO}</li>
 * </ul>
 */
@Mapper(componentModel = "spring")
public interface AdministrativeAreaDtoConverter {

    /**
     * 将行政区PO对象转换为DTO对象
     * @param po 行政区PO对象
     * @return 转换后的DTO对象
     */
    default AdministrativeAreaDTO convert(@NotNull AdministrativeAreaPO po) {
        AdministrativeAreaDTO dto = new AdministrativeAreaDTO();
        dto.setId(po.getId());
        dto.setSuperior(po.getSuperior());
        dto.setLevel(po.getLevel());
        // 行政区名字本地化设置
        Locale lang = LocaleContextHolder.getLocale();
        if (lang.equals(Locale.CHINA)) {
            dto.setName(po.getNameCN());
        } else {
            // 当前没有其他本地化方案当检测到非中国区的就直接返回英文
            dto.setName(po.getNameEN());
        }
        // 创建一个空的子节点列表
        dto.setAreas(new ArrayList<>());
        return dto;
    }

    default List<AdministrativeAreaDTO> convert(@NotNull List<AdministrativeAreaPO> pos) {
        List<AdministrativeAreaDTO> dtoList = new ArrayList<>();
        for (AdministrativeAreaPO po : pos) {
            dtoList.add(convert(po));
        }
        return dtoList;
    }
}
