package io.pifind.map3rd.amap.service.impl;

import io.pifind.common.response.R;
import io.pifind.common.util.UriSplicedUtils;
import io.pifind.map3rd.amap.model.constant.InfoCodeEnum;
import io.pifind.map3rd.amap.model.constant.ResponseStatusEnum;
import io.pifind.map3rd.amap.model.dto.AmapGeocodingDTO;
import io.pifind.map3rd.amap.model.dto.AmapReverseGeocodingDTO;
import io.pifind.map3rd.amap.model.qo.GeocodingQO;
import io.pifind.map3rd.amap.model.qo.ReverseGeocodingQO;
import io.pifind.map3rd.amap.model.wrapper.AmapGeocodingWrapper;
import io.pifind.map3rd.amap.model.wrapper.AmapReverseGeocodingWrapper;
import io.pifind.map3rd.amap.request.AmapApiTemplate;
import io.pifind.map3rd.amap.service.IAmapGeocodingService;
import io.pifind.map3rd.error.MapApiCode;
import io.pifind.map3rd.error.ThirdPartMapServiceException;
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
        AmapGeocodingWrapper wrapper = amapApiTemplate.getForObject(uri,AmapGeocodingWrapper.class);

        if (wrapper == null) {
            throw new ThirdPartMapServiceException(MapApiCode.CONNECTION_ERROR,"远程调用高德地图开放平台服务结果异常");
        }

        if (wrapper.getStatus().equals(ResponseStatusEnum.SUCCESS.code())) {
            return R.success(wrapper.getResult());
        } else {
            return R.failure(InfoCodeEnum.valueOf(wrapper.getInfo()));
        }

    }

    @Override
    public R<AmapReverseGeocodingDTO> reverseGeocoding(ReverseGeocodingQO qo) {

        String uri = UriSplicedUtils.spliceToString(qo);
        AmapReverseGeocodingWrapper wrapper = amapApiTemplate.getForObject(uri,AmapReverseGeocodingWrapper.class);

        if (wrapper == null) {
            throw new ThirdPartMapServiceException(MapApiCode.CONNECTION_ERROR,"远程调用高德地图开放平台服务结果异常");
        }

        if (wrapper.getStatus().equals(ResponseStatusEnum.SUCCESS.code())) {
            return R.success(wrapper.getResult());
        } else {
            return R.failure(InfoCodeEnum.valueOf(wrapper.getInfo()));
        }

    }

}
