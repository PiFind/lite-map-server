package io.pifind.mapserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.pifind.common.response.R;
import io.pifind.common.response.StandardCode;
import io.pifind.mapserver.converter.AdministrativeAreaDtoConverter;
import io.pifind.mapserver.error.PlaceCodeEnum;
import io.pifind.mapserver.mapper.AdministrativeAreaMapper;
import io.pifind.mapserver.model.AdministrativeAreaPO;
import io.pifind.place.api.IAdministrativeAreaService;
import io.pifind.place.model.AdministrativeAreaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;

@Service
public class AdministrativeAreaServiceImpl implements IAdministrativeAreaService {

    public static final int MAX_LEVEL = 10;

    @Autowired
    private AdministrativeAreaMapper administrativeAreaMapper;

    @Autowired
    private AdministrativeAreaDtoConverter administrativeAreaDtoConverter;

    @Override
    public R<Boolean> existAdministrativeAreaById(@NotNull Long id) {
        Boolean exist = administrativeAreaMapper.exists(
                new LambdaQueryWrapper<AdministrativeAreaPO>()
                        .eq(AdministrativeAreaPO::getId,id)
        );
        return R.success(exist);
    }

    @Override
    public R<AdministrativeAreaDTO> getAdministrativeAreaById(@NotNull Long id,@NotNull Integer layerLevel) {

        // 如果 ID = 0 说明获取的是国家
        if (id == 0L) {
            List<AdministrativeAreaPO> countries = administrativeAreaMapper.selectList(
                    new LambdaQueryWrapper<AdministrativeAreaPO>()
                            .eq(AdministrativeAreaPO::getSuperior,0)
            );

            AdministrativeAreaDTO administrativeAreaDTO = new AdministrativeAreaDTO();
            administrativeAreaDTO.setId(0L);

            // 行政区名字本地化设置
            Locale lang = LocaleContextHolder.getLocale();
            if (lang.equals(Locale.CHINA)) {
                administrativeAreaDTO.setName("世界");
            } else {
                // 当前没有其他本地化方案当检测到非中国区的就直接返回英文
                administrativeAreaDTO.setName("World");
            }
            administrativeAreaDTO.setLevel(-1);
            administrativeAreaDTO.setSuperior(0L);
            administrativeAreaDTO.setAreas(
                    administrativeAreaDtoConverter.convert(countries)
            );
            return R.success(administrativeAreaDTO);
        }

        // 创建行政区树的根节点
        AdministrativeAreaDTO tree = createAdministrativeAreaTree(id,layerLevel);

        // 如果为null
        if (tree == null) {
            return R.failure(PlaceCodeEnum.ADMINISTRATIVE_AREA_NOT_FOUND);
        }

        // 返回成功结果
        return R.success(tree);

    }

    @Override
    public R<String> getDetailedAddress(@NotNull Long id,@NotEmpty String separator) {

        // 获取要查询的行政区信息
        AdministrativeAreaPO areaPO = administrativeAreaMapper.selectById(id);
        if (areaPO == null) {
            return R.failure(PlaceCodeEnum.ADMINISTRATIVE_AREA_NOT_FOUND);
        }

        StringBuilder address = new StringBuilder();

        // 行政区名字本地化设置
        boolean cnFlag = LocaleContextHolder.getLocale().equals(Locale.CHINA);

        // 为避免死循环需要做一个限制啊
        for (int i = 0; i < MAX_LEVEL ;i ++) {

            if (cnFlag) {
                address.insert(0,areaPO.getNameCN());
            } else {
                address.insert(0,areaPO.getNameEN());
            }

            if (areaPO.getSuperior().equals(0L)) {
                break;
            } else {
                address.insert(0,separator);
            }

            areaPO =  administrativeAreaMapper.selectById(areaPO.getSuperior());

        }

        return R.success(
                StandardCode.SUCCESS_MESSAGE,
                address.toString()
        );

    }

    /**
     * 创建一个行政区树
     * <p>本质上等价于</p>
     * <p>
     *     {@code createAdministrativeAreaTree(rootId,0,maxHeight);}
     * </p>
     * @param rootId 根节点行政区的ID
     * @param maxHeight 最大的树高度
     * @return 行政区树的根节点
     */
    private AdministrativeAreaDTO createAdministrativeAreaTree(
            Long rootId,
            int maxHeight
    ) {
        return createAdministrativeAreaTree(rootId,0,maxHeight);
    }

    /**
     * 创建一个行政区树
     * @param rootId 根节点行政区的ID
     * @param currentHeight 当前的树的高度
     * @param maxHeight 最大的树高度
     * @return 行政区树的根节点
     */
    private AdministrativeAreaDTO createAdministrativeAreaTree(
            Long rootId,
            int currentHeight,
            int maxHeight
    ) {

        // 判断最大高度指标是否超过限制
        if (maxHeight > MAX_LEVEL) {
            maxHeight = MAX_LEVEL;
        }

        // 判断是否已经到了最大高度
        if (currentHeight >= maxHeight) {
            return null;
        }

        // 获取根节点的区域
        AdministrativeAreaPO rootArea = administrativeAreaMapper.selectById(rootId);
        if (rootArea == null) {
            return null;
        }

        // 转换为DTO根节点
        AdministrativeAreaDTO root = administrativeAreaDtoConverter.convert(rootArea);

        // 获取节点容器
        List<AdministrativeAreaDTO> nodes = root.getAreas() ;

        // 先获取当前行政区ID管辖的所有行政区
        List<AdministrativeAreaPO> areas = administrativeAreaMapper.selectList(
                new LambdaQueryWrapper<AdministrativeAreaPO>()
                        .eq(AdministrativeAreaPO::getSuperior,root.getId())
        );

        // 查找行政区管辖的行政区
        for (AdministrativeAreaPO area : areas) {

            // 创建一个节点
            AdministrativeAreaDTO node = createAdministrativeAreaTree(
                    area.getId(),
                    currentHeight + 1,
                    maxHeight);

            // 将节点加入总节点中
            if (node != null) {
                nodes.add(node);
            }
        }

        return root;
    }

}
