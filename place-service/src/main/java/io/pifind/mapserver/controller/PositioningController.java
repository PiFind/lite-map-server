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
@RequestMapping("/positioning")
public class PositioningController {

    @Autowired
    private IPositioningService positioningService;

    @GetMapping("/ip")
    public R<LocationDTO> getLocationByIP(
            @RequestParam("ip") String ip
    ) {
        return positioningService.getLocationByIP(ip);
    }

    @GetMapping("/coordinate")
    R<LocationDTO> getLocationByCoordinate(
            @RequestParam("longitude") Double longitude,
            @RequestParam("latitude") Double latitude,
            @RequestParam(value = "system",defaultValue = "WGS84",required = false) String system
    ) {
        GeographicCoordinateSystemEnum systemEnum =
                GeographicCoordinateSystemEnum.parse(system.toUpperCase());

        if (systemEnum == null) {
            return R.failure(PlaceCodeEnum.UNRESOLVED_COORDINATE_SYSTEM);
        }
        CoordinateDTO coordinateDTO = new CoordinateDTO();
        coordinateDTO.setLongitude(longitude);
        coordinateDTO.setLatitude(latitude);
        coordinateDTO.setSystem(systemEnum);
        return positioningService.getLocationByCoordinate(coordinateDTO);
    }

}
