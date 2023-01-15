package io.pifind.map3rd.google.service.impl;

import io.pifind.common.response.R;
import io.pifind.common.util.UriSplicedUtils;
import io.pifind.map3rd.error.MapApiCode;
import io.pifind.map3rd.error.ThirdPartMapServiceException;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GoogleGeocodingServiceImpl implements IGoogleGeocodingService {

    @Autowired
    private GoogleApiTemplate googleApiTemplate;

    @Override
    public R<List<GoogleGeocodingDTO>> geocoding(GeocodingQO qo) throws ThirdPartMapServiceException {

        String uri = UriSplicedUtils.spliceToString(qo);
        GeocodingWrapper wrapper = googleApiTemplate.getForObject(uri, GeocodingWrapper.class);

        // 检查返回体是否为空
        if (wrapper == null) {
            throw new ThirdPartMapServiceException(MapApiCode.CONNECTION_ERROR,"远程调用Google服务结果异常");
        }

        GeocodingStatusEnum status = GeocodingStatusEnum.valueOf(wrapper.getStatus());
        if (status.equals(GeocodingStatusEnum.OK)) {
            return R.success(wrapper.getResults());
        } else {
            return R.failure(status);
        }

    }

    @Override
    public R<List<GoogleGeocodingDTO>> reverseGeocoding(ReverseGeocodingQO qo) throws ThirdPartMapServiceException {

        String uri = UriSplicedUtils.spliceToString(qo);
        GeocodingWrapper wrapper = googleApiTemplate.getForObject(uri, GeocodingWrapper.class);

        // 检查返回体是否为空
        if (wrapper == null) {
            throw new ThirdPartMapServiceException(MapApiCode.CONNECTION_ERROR,"远程调用Google服务结果异常");
        }

        ReverseGeocodingStatusEnum status =
                ReverseGeocodingStatusEnum.valueOf(wrapper.getStatus());
        if (status.equals(ReverseGeocodingStatusEnum.OK)) {
            return R.success(wrapper.getResults());
        } else {
            return R.failure(status);
        }

    }

}
