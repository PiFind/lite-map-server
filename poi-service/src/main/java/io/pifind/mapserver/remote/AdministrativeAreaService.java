package io.pifind.mapserver.remote;

import io.pifind.common.response.R;
import io.pifind.common.util.UriSplicedUtils;
import io.pifind.mapserver.remote.request.MapServiceTemplate;
import io.pifind.place.api.IAdministrativeAreaService;
import io.pifind.place.model.AdministrativeAreaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.validation.constraints.NotNull;

/**
 * 行政区服务
 */
@Service
public class AdministrativeAreaService implements IAdministrativeAreaService {

    /**
     * Map Service 模板
     */
    @Autowired
    private MapServiceTemplate mapServiceTemplate;

    /**
     * Map Service URL
     */
    @Value("${pifind.map-service.url}")
    private String url;

    /**
     * 通过行政区ID获取行政区树
     * @param id 行政区ID
     * @return 行政区树
     */
    @Override
    public R<Boolean> existAdministrativeAreaById(@NotNull Long id) {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("id",id.toString());
        return mapServiceTemplate.getForStandardResponse(
                UriSplicedUtils.spliceToString(url + "area/exist",params),
                Boolean.class
        );
    }

    /**
     * 检查是否存在行政区ID
     * @param id 行政区ID
     * @return 返回值的类型为 {@link Boolean}
     * <ul>
     *     <li><b>存在行政区</b> - 返回 {@code true}</li>
     *     <li><b>不存在行政区</b> - 返回 {@code false}</li>
     * </ul>
     */
    @Override
    public R<AdministrativeAreaDTO> getAdministrativeAreaById(
            @NotNull Long id,
            @NotNull Integer deep) {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("id",id.toString());
        params.add("deep",deep.toString());
        String requestUrl = UriSplicedUtils.spliceToString(url + "area/get",params);
        return mapServiceTemplate.getForStandardResponse(
                requestUrl,
                AdministrativeAreaDTO.class
        );
    }

    /**
     * 获取详细地址
     * @param id 行政区ID
     * @param separator 行政区间的间隔夫（可选，默认为英文”,“）
     * @return 行政区ID对应的详细地址字符串
     */
    @Override
    public R<String> getDetailedAddress(@NotNull Long id, @NotNull String separator) {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("id",id.toString());
        params.add("separator",separator);
        return mapServiceTemplate.getForStandardResponse(
                UriSplicedUtils.spliceToString(url + "area/detail",params),
                String.class
        );
    }

}
