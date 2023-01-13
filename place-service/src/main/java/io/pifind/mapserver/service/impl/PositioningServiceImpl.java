package io.pifind.mapserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.pifind.common.response.R;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.map3rd.google.service.IGeocodingService;
import io.pifind.mapserver.converter.dto.LocationDtoConverter;
import io.pifind.mapserver.error.PlaceCodeEnum;
import io.pifind.mapserver.mapper.AdministrativeAreaMapper;
import io.pifind.mapserver.mapper.IP2LocationMapper;
import io.pifind.mapserver.model.po.AdministrativeAreaPO;
import io.pifind.mapserver.model.po.IP2LocationPO;
import io.pifind.mapserver.util.IPv4Utils;
import io.pifind.place.api.IPositioningService;
import io.pifind.place.model.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;

@Service
public class PositioningServiceImpl implements IPositioningService {

    @Autowired
    private IP2LocationMapper ip2LocationMapper;

    @Autowired
    private AdministrativeAreaMapper administrativeAreaMapper;

    @Autowired
    private LocationDtoConverter locationDtoConverter;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<LocationDTO> getLocationByIP(@NotEmpty String ip) {

        // 将IP转换为int类型的地址
        int ipAddress = IPv4Utils.stringToInt(ip);

        // 查询数据库
        IP2LocationPO ip2LocationPO = ip2LocationMapper.selectOne(
                new LambdaQueryWrapper<IP2LocationPO>()
                        .le(IP2LocationPO::getIpFrom,ipAddress)
                        .ge(IP2LocationPO::getIpTo,ipAddress)
        );
        if (ip2LocationPO == null) {
            return R.failure(PlaceCodeEnum.FAILED_TO_LOCATE_TO_ADMINISTRATIVE_AREA);
        }

        LocationDTO locationDTO = locationDtoConverter.convert(ip2LocationPO);

        // 如果为获取的地域ID为0，那么就通过谷歌地图API进行查询
        // 并将结果写入数据库
        if (locationDTO.getAdministrativeAreaId() == 0) {
            // 先去从 location 去匹配一下
            // 如果不能匹配到就再调用 google API
            Long areaId = getAdministrativeAreaIdByIP2Location(
                    ip2LocationPO
            );
            if (areaId == null) {
                areaId = getAdministrativeAreaIdByCoordinate(
                        locationDTO.getCoordinate()
                );
            }

            locationDTO.setAdministrativeAreaId(areaId);

            // 将数据写入数据库
            IP2LocationPO modifiedPO = new IP2LocationPO();
            modifiedPO.setAdministrativeAreaId(areaId);
            ip2LocationMapper.update(
                    modifiedPO,
                    new LambdaQueryWrapper<IP2LocationPO>()
                            .eq(IP2LocationPO::getIpFrom,ip2LocationPO.getIpFrom())
                            .eq(IP2LocationPO::getIpTo,ip2LocationPO.getIpTo())
            );

        }

        return R.success(locationDTO);
    }

    @Override
    public R<LocationDTO> getLocationByCoordinate(@NotNull CoordinateDTO coordinate) {

        // 通过坐标转换为行政区划ID
        Long areaId = getAdministrativeAreaIdByCoordinate(coordinate);
        if (areaId == null) {
            return R.failure(PlaceCodeEnum.FAILED_TO_LOCATE_TO_ADMINISTRATIVE_AREA);
        }

        // 通过行政区划ID查找行政区划对象
        AdministrativeAreaPO areaPO = administrativeAreaMapper.selectById(areaId);
        if (areaPO == null) {
            return R.failure(PlaceCodeEnum.ADMINISTRATIVE_AREA_NOT_FOUND);
        }

        // 将行政区划对象和坐标组合为标准地址

        LocationDTO locationDTO = new LocationDTO();

        Locale lang = LocaleContextHolder.getLocale();
        if (lang.equals(Locale.CHINESE)) {
            locationDTO.setName(areaPO.getNameCN());
        } else {
            locationDTO.setName(areaPO.getNameEN());
        }

        // 设置坐标
        locationDTO.setMissingCoordinate(false);
        locationDTO.setCoordinate(coordinate);

        return R.success(locationDTO);
    }

    /**
     * 调用 google API 进行查询地址
     * @param coordinate 需要查询的目标坐标
     * @return 定位的 AreaID
     */
    private Long getAdministrativeAreaIdByCoordinate(@NotNull CoordinateDTO coordinate) {

        return null;
    }

    /**
     * 根据 {@link IP2LocationPO} 对象来推测出可能的行政区划，可能不精确
     * @param ip2LocationPO {@link IP2LocationPO IP定位实体对象}
     * @return 返回结果为 {@link AdministrativeAreaPO} 的主键
     * <p>
     *     会根据IP2Location表进行查询具体的定位信息（行政区），然后一个一个的对比，
     *     最终获得一个行政区号，注意这个是不精确的的，其最差精度只能定位到国家。如果
     *     没有对比出任何结果，那么就会返回 {@code null}
     * </p>
     */
    private Long getAdministrativeAreaIdByIP2Location(IP2LocationPO ip2LocationPO) {
        AdministrativeAreaPO country = administrativeAreaMapper.selectOne(
                new LambdaQueryWrapper<AdministrativeAreaPO>()
                        .eq(AdministrativeAreaPO::getNameEN,ip2LocationPO.getCountryName())
                        .le(AdministrativeAreaPO::getId,500)
        );

        if (country != null) {
            AdministrativeAreaPO region = administrativeAreaMapper.selectOne(
                    new LambdaQueryWrapper<AdministrativeAreaPO>()
                            .eq(AdministrativeAreaPO::getNameEN, ip2LocationPO.getRegionName())
                            .le(AdministrativeAreaPO::getSuperior, country.getId())
            );
            if (region != null) {
                AdministrativeAreaPO area;

                // 如果城市名为域名
                if (ip2LocationPO.getCityName().equals(ip2LocationPO.getRegionName())) {
                    area = region;
                } else {
                    List<AdministrativeAreaPO> cities = administrativeAreaMapper.selectList(
                            new LambdaQueryWrapper<AdministrativeAreaPO>()
                                    .eq(AdministrativeAreaPO::getNameEN, ip2LocationPO.getCityName())
                                    .le(AdministrativeAreaPO::getSuperior, region.getId())
                    );

                    if (cities != null && cities.size() == 1) {
                        area = cities.get(0);
                    } else {
                        area = null;
                    }

                    // 如果没找到就以上一级区划为目标
                    if (area == null) {
                        area = region;
                    }
                }
                return area.getId();
            }
            return country.getId();
        }
        return null;
    }


}
