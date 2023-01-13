package io.pifind.mapserver.service;

import io.pifind.common.response.R;
import io.pifind.map3rd.google.model.dto.GeocodingDTO;
import io.pifind.map3rd.google.model.qo.ReverseGeocodingQO;
import io.pifind.map3rd.google.service.IGeocodingService;
import io.pifind.mapserver.PlaceApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest(classes = {PlaceApplicationTest.class})
public class GoogleGeocodingServiceTest {

    @Autowired
    private IGeocodingService googleGeocodingService;

    @Test
    public void test() {
        ReverseGeocodingQO qo = new ReverseGeocodingQO();
        qo.setLatlng("40.714224,-73.961452");
        R<List<GeocodingDTO>> result = googleGeocodingService.reverseGeocoding(qo);
        if (result != null) {
            log.info("{}",result);
        }
    }

}
