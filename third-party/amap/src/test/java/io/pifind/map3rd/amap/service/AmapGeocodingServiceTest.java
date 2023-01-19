package io.pifind.map3rd.amap.service;

import io.pifind.map3rd.amap.model.qo.GeocodingQO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AmapGeocodingServiceTest {

    @Autowired
    private IAmapGeocodingService amapGeocodingService;

    @Test
    public void testGeocoding() {
        GeocodingQO qo = new GeocodingQO("中国");
        amapGeocodingService.geocoding(qo);
    }

}
