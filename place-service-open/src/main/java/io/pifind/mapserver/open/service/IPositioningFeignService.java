package io.pifind.mapserver.open.service;

import io.pifind.common.response.R;
import io.pifind.mapserver.open.support.PlaceServiceAPI;
import io.pifind.place.model.LocationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "place-positioning-service",url = PlaceServiceAPI.URL)
public interface IPositioningFeignService {

    /**
     * 通过IP进行定位
     * @param ip 需要定位的目标IP
     * @return 返回值类型为 {@link LocationDTO}
     * <ul>
     *     <li><b>IP正确</b> 且 <b>数据库存在记录</b> - 返回 {@link LocationDTO 位置实体对象}</li>
     *     <li><b>IP不正确</b> 或 <b>数据库不存在记录</b> - 返回 {@code null}</li>
     * </ul>
     */
    @GetMapping("/ip")
    R<LocationDTO> getLocationByIP(
            @RequestParam("ip") String ip
    );

    /**
     * 根据坐标进行定位
     * @param lng 经度
     * @param lat 纬度
     * @param system 坐标系
     * @return 返回值类型为 {@link LocationDTO}
     * <ul>
     *     <li><b>坐标正确</b> 且 <b>数据库中存在坐标数据</b> - 返回 {@link LocationDTO 位置实体对象}</li>
     *     <li><b>坐标错误</b> 或 <b>数据库中不存在坐标数据</b> - 返回 {@code null}</li>
     * </ul>
     */
    @GetMapping("/coordinate")
    R<LocationDTO> getLocationByCoordinate(
            @RequestParam("lng") Double lng,
            @RequestParam("lat") Double lat,
            @RequestParam(value = "system",defaultValue = "WGS84",required = false) String system
    );

}
