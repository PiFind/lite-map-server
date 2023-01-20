package io.pifind.map3rd.api;

import io.pifind.common.response.R;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.map3rd.model.GeocodingDTO;
import io.pifind.map3rd.model.component.GeographicalTargetDTO;
import io.pifind.map3rd.model.ReverseGeocodingDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标准地理编码服务
 * <p>
 *     第三方实现类需要在 {@link Service} 注解内以下述方式命名组件 ：
 *     <p>{@code @Service("第三方服务提供商-GeocodingService")}</p>
 *     例如：
 *     <p>{@code @Service("Google-GeocodingService")}</p>
 * </p>
 */
public interface IGeocodingService {

    /**
     * 地理编码
     * @param address 地址
     * @param language 使用的语言，如果为 {@code null}，那么将自动选择本地语言
     * @return 定位信息
     */
    R<GeocodingDTO> geocoding(@NotEmpty String address, String language);

    /**
     * 反向地理编码
     * @param coordinate 坐标
     * @param language 使用的语言，如果为 {@code null}，那么将自动选择本地语言
     * @return 反向地理编码信息
     */
    R<ReverseGeocodingDTO> reverseGeocoding(@NotNull CoordinateDTO coordinate,String language);

}
