package io.pifind.mapserver.open.controller;

import io.pifind.common.response.R;
import io.pifind.mapserver.open.service.IPositioningFeignService;
import io.pifind.place.model.LocationDTO;
import io.pifind.role.annotation.RequestPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定位控制器
 */
@RestController
@RequestMapping("/v1/positioning")
public class PositioningController {

    @Autowired
    private IPositioningFeignService positioningFeignService;

    /**
     * 根据IP进行定位
     * @param ip ip地址字符串
     * @return 定位实体类对象
     */
    @GetMapping("/ip")
    @RequestPermission(name = "positioning.ip",description = "根据IP进行定位")
    public R<LocationDTO> getLocationByIP(
            @RequestParam("ip") String ip
    ) {
        return positioningFeignService.getLocationByIP(ip);
    }


    /**
     * 根据坐标进行定位
     * @param lng 经度
     * @param lat 纬度
     * @param system 坐标系
     * @return 定位实体类对象
     */
    @GetMapping("/coordinate")
    @RequestPermission(name = "positioning.coordinate",description = "根据坐标进行定位")
    public R<LocationDTO> getLocationByCoordinate(
            @RequestParam("lng") Double lng,
            @RequestParam("lat") Double lat,
            @RequestParam(value = "system",defaultValue = "WGS84",required = false) String system
    ) {
        return positioningFeignService.getLocationByCoordinate(lng,lat,system);
    }

}
