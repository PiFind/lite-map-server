package io.pifind.map3rd.amap.service.impl;

import io.pifind.common.response.R;
import io.pifind.common.util.UriSplicedUtils;
import io.pifind.map3rd.amap.model.dto.AmapGeocodingDTO;
import io.pifind.map3rd.amap.model.dto.AmapReverseGeocodingDTO;
import io.pifind.map3rd.amap.model.qo.GeocodingQO;
import io.pifind.map3rd.amap.model.qo.ReverseGeocodingQO;
import io.pifind.map3rd.amap.request.AmapApiTemplate;
import io.pifind.map3rd.amap.service.IAmapGeocodingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AmapGeocodingServiceImpl implements IAmapGeocodingService {

    @Autowired
    private AmapApiTemplate amapApiTemplate;

    @Override
    public R<AmapGeocodingDTO> geocoding(GeocodingQO qo) {
        String uri = UriSplicedUtils.spliceToString(qo);


        return null;
    }

    @Override
    public R<AmapReverseGeocodingDTO> reverseGeocoding(ReverseGeocodingQO qo) {

        return null;
    }

}
