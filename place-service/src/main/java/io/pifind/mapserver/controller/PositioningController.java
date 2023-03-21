package io.pifind.mapserver.controller;

import io.pifind.common.response.R;
import io.pifind.map.constant.GeographicCoordinateSystemEnum;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.mapserver.error.PlaceCodeEnum;
import io.pifind.place.api.IPositioningService;
import io.pifind.place.model.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/positioning")
public class PositioningController {

    @Autowired
    private IPositioningService positioningService;

    /**
     * 根据IP进行定位
     * @param ip ip地址字符串
     * @return 定位实体类对象
     */
    @GetMapping("/ip")
    public R<LocationDTO> getLocationByIP(
            @RequestParam("ip") String ip
    ) {
        return positioningService.getLocationByIP(ip);
    }


    @GetMapping("/coordinate")
    R<LocationDTO> getLocationByCoordinate(
            @RequestParam("lng") Double lng,
            @RequestParam("lat") Double lat,
            @RequestParam(value = "system",defaultValue = "WGS84",required = false) String system
    ) {

        // 将传入的格式转换为枚举
        GeographicCoordinateSystemEnum systemEnum =
                GeographicCoordinateSystemEnum.parse(system.toUpperCase());

        // 如果不支持，那么就返回 UNSUPPORTED_COORDINATE_SYSTEM 错误
        if (systemEnum == null) {
            return R.failure(PlaceCodeEnum.UNSUPPORTED_COORDINATE_SYSTEM);
        }

        // 创建一个坐标系对象
        CoordinateDTO coordinateDTO = new CoordinateDTO();
        coordinateDTO.setLongitude(lng);
        coordinateDTO.setLatitude(lat);
        coordinateDTO.setSystem(systemEnum);

        return positioningService.getLocationByCoordinate(coordinateDTO);
    }

}
