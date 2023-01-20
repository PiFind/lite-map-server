package io.pifind.map3rd.amap.converter;

import io.pifind.map.model.CoordinateDTO;
import org.mapstruct.Mapper;

import javax.validation.constraints.NotEmpty;

@Mapper(componentModel = "spring")
public interface CoordinateDtoConverter {

    default CoordinateDTO convert(@NotEmpty String location) {
        String[] points = location.split(",");
        double lng = Double.parseDouble(points[0]);
        double lat = Double.parseDouble(points[1]);
        return new CoordinateDTO(lng,lat);
    }

}
