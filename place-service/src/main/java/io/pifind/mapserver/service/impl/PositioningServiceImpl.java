package io.pifind.mapserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.pifind.common.response.R;
import io.pifind.common.response.StandardCode;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.map3rd.api.IGeocodingService;
import io.pifind.map3rd.model.ReverseGeocodingDTO;
import io.pifind.map3rd.model.component.SingleDistrictDTO;
import io.pifind.mapserver.converter.LocationDtoConverter;
import io.pifind.mapserver.error.PlaceCodeEnum;
import io.pifind.mapserver.mapper.AdministrativeAreaMapper;
import io.pifind.mapserver.mapper.CountryMapper;
import io.pifind.mapserver.mapper.IP2LocationMapper;
import io.pifind.mapserver.model.AdministrativeAreaPO;
import io.pifind.mapserver.model.CountryPO;
import io.pifind.mapserver.model.IP2LocationPO;
import io.pifind.mapserver.util.ChinaSpecialCityUtils;
import io.pifind.mapserver.util.IPv4Utils;
import io.pifind.place.api.IPositioningService;
import io.pifind.place.model.LocationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class PositioningServiceImpl implements IPositioningService {

    @Autowired
    private IP2LocationMapper ip2LocationMapper;

    @Autowired
    private AdministrativeAreaMapper administrativeAreaMapper;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private LocationDtoConverter locationDtoConverter;

    /** Google 的地理编码服务 */
    @Resource(name = "Google-GeocodingService")
    private IGeocodingService globalGeocodingService;

    // 下面的注释是为使用的高德的 地理编码服务，如果只做中国区的服务，
    // 那么使用高德将更加精确
    // @Resource(name = "Amap-GeocodingService")
    // private IGeocodingService chinaGeocodingService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<LocationDTO> getLocationByIP(@NotEmpty String ip) {

        if (!IPv4Utils.isIPv4Address(ip)) {
            return R.failure(PlaceCodeEnum.WRONG_IPV4_ADDRESS);
        }

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

        // 如果为获取的地域ID为0，那么就通过第三方地图API进行查询
        // 并将结果写入数据库
        if (locationDTO.getAdministrativeAreaId() == 0) {

            // 先去从 location 去匹配一下
            // 如果不能匹配到就再调用第三方
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
        // 获取对应的国家
        CountryPO countryPO = countryMapper.selectById(
                Integer.valueOf(String.valueOf(areaId).substring(0,3))
        );

        if (areaPO == null) {
            return R.failure(PlaceCodeEnum.ADMINISTRATIVE_AREA_NOT_FOUND);
        }

        // 将行政区划对象和坐标组合为标准地址
        LocationDTO locationDTO = new LocationDTO();

        Locale lang = LocaleContextHolder.getLocale();
        if (lang.equals(Locale.CHINA)) {
            locationDTO.setName(areaPO.getNameCN());
        } else {
            locationDTO.setName(areaPO.getNameEN());
        }

        // 设置国家
        locationDTO.setCountry(countryPO.getISO2());

        // 设置坐标
        locationDTO.setMissingCoordinate(false);
        locationDTO.setCoordinate(coordinate);

        // 设置行政区划ID
        locationDTO.setAdministrativeAreaId(areaId);

        return R.success(locationDTO);
    }

    /**
     * 调用第三方进行查询地址
     * @param coordinate 需要查询的目标坐标
     * @return 定位的 AreaID
     */
    private Long getAdministrativeAreaIdByCoordinate(@NotNull CoordinateDTO coordinate) {

        // 由于数据库中只记录了中文和英文的行政区划名，为了定位的准确性这里就返回英语
        R<ReverseGeocodingDTO> result =
                globalGeocodingService.reverseGeocoding(coordinate,Locale.ENGLISH.getLanguage());

        if (result.getCode() != StandardCode.SUCCESS) {
            // 做错误日志
            log.error("{}",result.getCode());
            return null;
        }

        // 根据返回结果定位到行政区的 AdministrativeAreaID
        ReverseGeocodingDTO reverseGeocoding = result.getData();

        // 先检查有没有中国的特别行政区的代码
        String countryCode = reverseGeocoding.getCountryCode();
        if(ChinaSpecialCityUtils.check(countryCode)) {
            return ChinaSpecialCityUtils.getAdministrativeAreaId(countryCode);
        }

        // 获取国家代码对应的国家
        CountryPO countryPO = countryMapper.selectOne(
                new LambdaQueryWrapper<CountryPO>().eq(CountryPO::getISO2,countryCode)
        );
        if(countryPO == null) {
            return null;
        }

        Long areaId = null;
        Long superior = 0L;

        // 根据返回的区划进行遍历
        for (SingleDistrictDTO district : reverseGeocoding.getDistricts()) {

            List<AdministrativeAreaPO> areas =  administrativeAreaMapper.selectList(
                    new LambdaQueryWrapper<AdministrativeAreaPO>()
                            .likeLeft(AdministrativeAreaPO::getNameEN,district.getName().trim())
                            .eq(AdministrativeAreaPO::getLevel,district.getLevel())
                            // 因为是按照level的从小到大（从0开始）的顺序排的，所以可以直接将上级行政区ID作为搜索条件
                            .eq(AdministrativeAreaPO::getSuperior,superior)
            );

            // 如果能找到，那么就会继续往下找
            if (!areas.isEmpty()) {
                AdministrativeAreaPO area = areas.get(0);
                superior = area.getId();
                areaId = area.getId();
            } else {
                break;
            }
        }

        return areaId;
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

        // 检查中国行政区
        String countryCode = ip2LocationPO.getCountryCode();
        if (ChinaSpecialCityUtils.check(countryCode)) {
            return ChinaSpecialCityUtils.getAdministrativeAreaId(countryCode);
        }

        // 通过国家表查询国家
        CountryPO countryPO = countryMapper.selectOne(
                new LambdaQueryWrapper<CountryPO>()
                        .eq(CountryPO::getISO2,countryCode)
        );

        // 如果没找到国家直接返回 null
        if (countryPO == null) {
            return null;
        }

        AdministrativeAreaPO country = administrativeAreaMapper.selectOne(
                new LambdaQueryWrapper<AdministrativeAreaPO>()
                        .eq(AdministrativeAreaPO::getId,countryPO.getId())
        );

        /* 下面是一个查找行政区的算法
         * 在能定位到国家的情况下，如果能查找到一级行政区，那么就继续找下一级行政区
         * 如果找不到，就返回上一级行政区的值，如果国家都定位不到就返回 null
         */

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
