package io.pifind.map3rd.google.converter;

import io.pifind.map.model.CoordinateDTO;
import io.pifind.map3rd.google.model.dto.GoogleCoordinateDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class CoordinateDtoConverterTest {

    @Autowired
    private CoordinateDtoConverter coordinateDtoConverter;

    @Test
    public void testConvert() {
        GoogleCoordinateDTO googleCoordinateDTO = new GoogleCoordinateDTO();
        googleCoordinateDTO.setLat(10.0);
        googleCoordinateDTO.setLng(15.0);

        CoordinateDTO coordinateDTO = coordinateDtoConverter.convert(googleCoordinateDTO);
        log.info("{}",coordinateDTO);
    }

}
