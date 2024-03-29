package io.pifind.mapserver.converter;

import io.pifind.map.constant.GeographicCoordinateSystemEnum;
import io.pifind.map.model.CoordinateDTO;

import io.pifind.mapserver.model.IP2LocationPO;
import io.pifind.place.model.LocationDTO;
import org.mapstruct.Mapper;

/**
 * {@link LocationDTO} 实体类转换器
 * <p>支持转换的对象如下：</p>
 * <ul>
 *     <li>{@link IP2LocationPO}</li>
 * </ul>
 */
@Mapper(componentModel = "spring")
public interface LocationDtoConverter {

    default LocationDTO convert(IP2LocationPO po) {
        LocationDTO dto = new LocationDTO();

        dto.setName(po.getCityName());
        dto.setCountry(po.getCountryCode());

        // 构建坐标系
        CoordinateDTO coordinate = new CoordinateDTO();
        coordinate.setLatitude(po.getLatitude());
        coordinate.setLongitude(po.getLongitude());
        // 设置为国际标准坐标系
        coordinate.setSystem(GeographicCoordinateSystemEnum.WGS84);
        dto.setCoordinate(coordinate);
        dto.setMissingCoordinate(false);

        // 设置行政区ID
        dto.setAdministrativeAreaId(po.getAdministrativeAreaId());

        return dto;
    }

}
