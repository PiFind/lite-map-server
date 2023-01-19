package io.pifind.map3rd.amap.service;

import io.pifind.common.response.R;
import io.pifind.map3rd.amap.model.dto.AmapGeocodingDTO;
import io.pifind.map3rd.amap.model.dto.AmapReverseGeocodingDTO;
import io.pifind.map3rd.amap.model.qo.GeocodingQO;
import io.pifind.map3rd.amap.model.qo.ReverseGeocodingQO;
import io.pifind.map3rd.error.ThirdPartMapServiceException;

import javax.validation.constraints.NotNull;

public interface IAmapGeocodingService {

    /**
     * 地理编码，参考：
     * <a href="https://lbs.amap.com/api/webservice/guide/api/georegeo#geo">高德地图开放平台-地理编码</a>
     * @param qo {@link AmapGeocodingDTO 地理编码实体对象}
     * @return 地理编码查询结果
     * @throws ThirdPartMapServiceException 第三方地图服务调用异常
     */
    R<AmapGeocodingDTO> geocoding(@NotNull GeocodingQO qo)  throws ThirdPartMapServiceException;

    /**
     * 反向地理编码，参考：
     * <a href="https://lbs.amap.com/api/webservice/guide/api/georegeo#regeo">高德地图开放平台-逆地理编码</a>
     * @param qo {@link AmapGeocodingDTO 反向地理编码实体对象}
     * @return 反向地理编码查询结果(与地理编码查询结果相同)
     * @throws ThirdPartMapServiceException 第三方地图服务调用异常
     */
    R<AmapReverseGeocodingDTO> reverseGeocoding(@NotNull ReverseGeocodingQO qo)  throws ThirdPartMapServiceException;

}
