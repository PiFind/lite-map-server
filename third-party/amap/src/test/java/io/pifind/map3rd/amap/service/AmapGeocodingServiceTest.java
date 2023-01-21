package io.pifind.map3rd.amap.service;

import io.pifind.common.response.R;
import io.pifind.common.response.StandardCode;
import io.pifind.map3rd.amap.model.dto.AmapGeocodingDTO;
import io.pifind.map3rd.amap.model.dto.AmapReverseGeocodingDTO;
import io.pifind.map3rd.amap.model.qo.GeocodingQO;
import io.pifind.map3rd.amap.model.qo.ReverseGeocodingQO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class AmapGeocodingServiceTest {

    @Autowired
    private IAmapGeocodingService amapGeocodingService;

    @Test
    public void testGeocoding() {
        GeocodingQO qo = new GeocodingQO("中国");
        R<AmapGeocodingDTO> result =  amapGeocodingService.geocoding(qo);
        if (result.getCode() == StandardCode.SUCCESS) {
            log.info("{}",result.getData());
        } else {
            throw new RuntimeException(result.toString());
        }
    }

    @Test
    public void testReverseGeocoding() {
        ReverseGeocodingQO qo = new ReverseGeocodingQO(112.362075,24.015539);
        R<AmapReverseGeocodingDTO> result = amapGeocodingService.reverseGeocoding(qo);
        if (result.getCode() == StandardCode.SUCCESS) {
            log.info("{}",result);
        } else {
            throw new RuntimeException(result.toString());
        }
    }

}
