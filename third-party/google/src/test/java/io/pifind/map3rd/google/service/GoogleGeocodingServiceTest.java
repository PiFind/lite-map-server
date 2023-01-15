package io.pifind.map3rd.google.service;

import io.pifind.common.response.R;
import io.pifind.map3rd.google.ApplicationTest;
import io.pifind.map3rd.google.model.dto.GoogleGeocodingDTO;
import io.pifind.map3rd.google.model.qo.ReverseGeocodingQO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest(classes = {ApplicationTest.class})
public class GoogleGeocodingServiceTest {

    @Autowired
    private IGoogleGeocodingService googleGeocodingService;

    @Test
    public void test() {
        ReverseGeocodingQO qo = new ReverseGeocodingQO();
        qo.setLatlng("40.714224,-73.961452");
        R<List<GoogleGeocodingDTO>> result = googleGeocodingService.reverseGeocoding(qo);
        if (result != null) {
            log.info("{}",result);
        }
    }

}
