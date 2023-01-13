package io.pifind.map3rd.google.service.impl;

import io.pifind.common.response.R;
import io.pifind.common.util.UriSplicedUtils;
import io.pifind.map3rd.google.model.constant.GeocodingStatusEnum;
import io.pifind.map3rd.google.model.constant.ReverseGeocodingStatusEnum;
import io.pifind.map3rd.google.model.dto.GeocodingDTO;
import io.pifind.map3rd.google.model.qo.GeocodingQO;
import io.pifind.map3rd.google.model.qo.ReverseGeocodingQO;
import io.pifind.map3rd.google.model.wrapper.GeocodingWrapper;
import io.pifind.map3rd.google.request.GoogleApiTemplate;
import io.pifind.map3rd.google.service.IGeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeocodingServiceImpl implements IGeocodingService {

    @Autowired
    private GoogleApiTemplate googleApiTemplate;

    @Override
    public R<List<GeocodingDTO>> geocoding(GeocodingQO qo) {

        String uri = UriSplicedUtils.spliceToString(qo);
        GeocodingWrapper response = googleApiTemplate.getForObject(uri, GeocodingWrapper.class);

        // 如果未能请求到结果，那么返回错误信息
        if (response == null) {
            return R.failure();
        }

        GeocodingStatusEnum status = GeocodingStatusEnum.valueOf(response.getStatus());
        if (status.equals(GeocodingStatusEnum.OK)) {
            return R.success(response.getResult());
        } else {
            return R.failure(status.name());
        }

    }

    @Override
    public R<List<GeocodingDTO>> reverseGeocoding(ReverseGeocodingQO qo) {

        String uri = UriSplicedUtils.spliceToString(qo);
        GeocodingWrapper response = googleApiTemplate.getForObject(uri, GeocodingWrapper.class);

        // 如果未能请求到结果，那么返回错误信息
        if (response == null) {
            return R.failure();
        }

        ReverseGeocodingStatusEnum status = ReverseGeocodingStatusEnum.valueOf(response.getStatus());
        if (status.equals(ReverseGeocodingStatusEnum.OK)) {
            return R.success(response.getResult());
        } else {
            return R.failure(status.name());
        }

    }

}
