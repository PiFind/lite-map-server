package io.pifind.mapserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.pifind.common.i18n.MessageBundle;
import io.pifind.common.response.R;
import io.pifind.mapserver.converter.dto.AdministrativeAreaDtoConverter;
import io.pifind.mapserver.mapper.AdministrativeAreaMapper;
import io.pifind.mapserver.model.po.AdministrativeAreaPO;
import io.pifind.place.api.IAdministrativeAreaService;
import io.pifind.place.model.AdministrativeAreaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;

@Service
public class AdministrativeAreaServiceImpl implements IAdministrativeAreaService {

    public static final int MAX_LEVEL = 10;

    @Resource(name = "PlaceService-MessageBundle")
    private MessageBundle messageBundle;

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
        return R.success(
                messageBundle.get("AdministrativeAreaService.Success"),
                exist
        );
    }

    @Override
    public R<AdministrativeAreaDTO> getAdministrativeAreaById(@NotNull Long id,@NotNull Integer layerLevel) {

        // 创建行政区树的根节点
        AdministrativeAreaDTO tree = createAdministrativeAreaTree(id,layerLevel);

        // 如果为null
        if (tree == null) {
            return R.failure(
                    messageBundle.get("AdministrativeAreaService.AdministrativeAreaNotFound")
            );
        }

        // 返回成功结果
        return R.success(
                messageBundle.get("AdministrativeAreaService.Success"),
                tree
        );

    }

    @Override
    public R<String> getDetailedAddress(@NotNull Long id,@NotEmpty String separator) {

        // 获取要查询的行政区信息
        AdministrativeAreaPO areaPO = administrativeAreaMapper.selectById(id);
        if (areaPO == null) {
            return R.failure(
                    messageBundle.get("AdministrativeAreaService.AdministrativeAreaNotFound")
            );
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

            areaPO =  administrativeAreaMapper.selectById(areaPO.getSuperior());
            if (areaPO.getSuperior().equals(0L)) {
                break;
            } else {
                address.insert(0,separator);
            }

        }

        return R.success(
                messageBundle.get("AdministrativeAreaService.Success"),
                address.toString()
        );

    }

    /**
     * 创建一个行政区树
     * <p>
     *     本质上等价于
     *     <p>
     *         {@code createAdministrativeAreaTree(rootId,0,maxHeight);}
     *     </p>
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
