package io.pifind.map3rd.google.service.impl;

import io.pifind.common.response.R;
import io.pifind.common.util.UriSplicedUtils;
import io.pifind.map3rd.google.model.constant.GeocodingStatusEnum;
import io.pifind.map3rd.google.model.constant.ReverseGeocodingStatusEnum;
import io.pifind.map3rd.google.model.dto.GoogleGeocodingDTO;
import io.pifind.map3rd.google.model.qo.GeocodingQO;
import io.pifind.map3rd.google.model.qo.ReverseGeocodingQO;
import io.pifind.map3rd.google.model.wrapper.GeocodingWrapper;
import io.pifind.map3rd.google.request.GoogleApiTemplate;
import io.pifind.map3rd.google.service.IGoogleGeocodingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GoogleGeocodingServiceImpl implements IGoogleGeocodingService {

    @Autowired
    private GoogleApiTemplate googleApiTemplate;

    @Override
    public R<List<GoogleGeocodingDTO>> geocoding(GeocodingQO qo) {

        String uri = UriSplicedUtils.spliceToString(qo);
        ResponseEntity<GeocodingWrapper> response = googleApiTemplate.getForEntity(uri, GeocodingWrapper.class);

        // 如果未能请求到结果，那么返回错误信息
        if (!response.getStatusCode().is2xxSuccessful()) {
            return R.failure();
        }

        // 检查返回体是否为空
        GeocodingWrapper responseBody = response.getBody();
        if (responseBody == null) {
           return R.failure();
        }

        GeocodingStatusEnum status = GeocodingStatusEnum.valueOf(responseBody.getStatus());
        if (status.equals(GeocodingStatusEnum.OK)) {
            return R.success(responseBody.getResults());
        } else {
            return R.failure(status.name());
        }

    }

    @Override
    public R<List<GoogleGeocodingDTO>> reverseGeocoding(ReverseGeocodingQO qo) {

        String uri = UriSplicedUtils.spliceToString(qo);
        ResponseEntity<GeocodingWrapper> response = googleApiTemplate.getForEntity(uri, GeocodingWrapper.class);

        // 如果未能请求到结果，那么返回错误信息
        if (!response.getStatusCode().is2xxSuccessful()) {
            return R.failure();
        }

        // 检查返回体是否为空
        GeocodingWrapper responseBody = response.getBody();
        if (responseBody == null) {
            return R.failure();
        }

        ReverseGeocodingStatusEnum status = ReverseGeocodingStatusEnum.valueOf(responseBody.getStatus());
        if (status.equals(ReverseGeocodingStatusEnum.OK)) {
            return R.success(responseBody.getResults());
        } else {
            return R.failure(status.name());
        }

    }

}
