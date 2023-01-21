package io.pifind.map3rd.amap.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.pifind.map3rd.amap.model.dto.component.*;
import lombok.Data;

import java.util.List;

/**
 * 高德地图反向地理编码（逆地理编码）返回值实体类对象
 */
@Data
public class AmapReverseGeocodingDTO {

    /**
     * 逆地理编码列表
     */
    @JsonProperty("regeocode")
    private ReverseGeocode reverseGeocode;

    @Data
    public static class ReverseGeocode {

        /**
         * 格式化的地址数据
         */
        @JsonProperty("formatted_address")
        private String formattedAddress;

        /**
         * 地址元素列表
         */
        private AmapAddressDTO addressComponent;

        /**
         * 道路信息列表
         * <p>请求参数 extensions 为 all 时返回如下内容</p>
         */
        private List<AmapRoadDTO> roads;

        /**
         * 道路交叉口列表
         * <p>请求参数 extensions 为 all 时返回如下内容</p>
         */
        @JsonProperty("roadinters")
        private List<AmapRoadIntersectionDTO> roadIntersections;

        /**
         * POI信息列表
         * <p>请求参数 extensions 为 all 时返回如下内容</p>
         */
        private List<AmapSimplifiedPoiDTO> pois;

        /**
         * AOI信息列表
         * <p>请求参数 extensions 为 all 时返回如下内容</p>
         */
        private List<AmapSimplifiedAoiDTO> aois;

    }


}
