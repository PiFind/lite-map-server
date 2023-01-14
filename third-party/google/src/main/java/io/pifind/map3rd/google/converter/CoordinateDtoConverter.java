package io.pifind.map3rd.google.converter;

import io.pifind.map.model.CoordinateDTO;
import io.pifind.map3rd.google.model.dto.GoogleCoordinateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CoordinateDtoConverter {

    @Mappings({
            @Mapping(target = "latitude",source = "lat"),
            @Mapping(target = "longitude",source = "lng"),
            @Mapping(target = "system",constant = "WGS84")
    })
    CoordinateDTO convert(GoogleCoordinateDTO googleCoordinate);

}
