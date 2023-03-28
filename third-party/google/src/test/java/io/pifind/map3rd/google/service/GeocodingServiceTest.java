package io.pifind.map3rd.google.service;

import io.pifind.common.response.R;
import io.pifind.common.response.StandardCode;
import io.pifind.map.constant.GeographicCoordinateSystemEnum;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.map3rd.api.IGeocodingService;
import io.pifind.map3rd.google.ApplicationTest;
import io.pifind.map3rd.model.GeocodingDTO;
import io.pifind.map3rd.model.component.GeographicalTargetDTO;
import io.pifind.map3rd.model.ReverseGeocodingDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

@Slf4j
@SpringBootTest(classes = ApplicationTest.class)
public class GeocodingServiceTest {

    @Resource(name = "Google-GeocodingService")
    private IGeocodingService geocodingService;

    // @Test
    public void testGeocoding() {
        R<GeocodingDTO> result =
               geocodingService.geocoding("China,Henan,puyang,华龙区,大庆中路与任丘路交叉口", Locale.CHINA.getLanguage());
               // geocodingService.geocoding("Japan Osaka-shi Osakajo, ３−1 大阪城ホール前 噴水広場",Locale.JAPAN.getLanguage());

        if (result.getCode() == StandardCode.SUCCESS) {
            log.info("{}",result.getData());
        } else {
            throw new RuntimeException(result.toString());
        }
    }

    // @Test
    public void testReverseGeocoding() {
        CoordinateDTO coordinate = new CoordinateDTO();
        coordinate.setLatitude(34.6896762);
        coordinate.setLongitude(135.5317349);
        coordinate.setSystem(GeographicCoordinateSystemEnum.WGS84);

        // 反向地理定位
        R<ReverseGeocodingDTO> result = geocodingService.reverseGeocoding(coordinate,Locale.ENGLISH.getLanguage());

        if (result.getCode() == StandardCode.SUCCESS) {
            log.info("{}",result.getData());
        } else {
            throw new RuntimeException(result.toString());
        }

    }

}
