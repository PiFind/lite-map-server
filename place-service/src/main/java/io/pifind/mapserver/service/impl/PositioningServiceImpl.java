package io.pifind.mapserver.service.impl;

import io.pifind.common.response.R;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.mapserver.mapper.IP2LocationMapper;
import io.pifind.place.api.IPositioningService;
import io.pifind.place.model.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Service
public class PositioningServiceImpl implements IPositioningService {

    @Autowired
    private IP2LocationMapper ip2LocationMapper;

    @Override
    public R<LocationDTO> getLocationByIP(@NotEmpty String ip) {

        return null;
    }

    @Override
    public R<LocationDTO> getLocationByCoordinate(@NotNull CoordinateDTO coordinate) {


        return null;
    }

}
