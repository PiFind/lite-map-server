package io.pifind.map3rd.amap.model.dto.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 高德地图地址组件
 */
@Data
public class AmapAddressDTO {

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市编码
     */
    @JsonProperty("citycode")
    private String cityCode;

    /**
     * 坐标点所在区
     */
    private String district;

    /**
     * 行政区编码
     */
    private String adcode;

    /**
     * 坐标点所在乡镇/街道（此街道为社区街道，不是道路信息）
     * <p>例如：燕园街道</p>
     */
    private String township;

    /**
     * 乡镇街道编码
     * <p>例如：110101001000</p>
     */
    @JsonProperty("towncode")
    private String townCode;

    /**
     * 社区信息列表
     */
    private SimplePoiInformation neighborhood;

    /**
     * 楼信息列表
     */
    private SimplePoiInformation building;

    @Data
    public static class SimplePoiInformation {

        /**
         * 名称
         */
        private String name;

        /**
         * 类型
         * <p>例如：科教文化服务;学校;高等院校</p>
         */
        private String type;

    }

    /**
     * 门牌信息
     */
    private AmapStreetDTO streetNumber;

    /**
     * 所属海域信息
     */
    private String seaArea;

    private List<BusinessArea> businessAreas;

    /**
     * 商圈信息
     */
    @Data
    public static class BusinessArea {

        /**
         * 商圈中心点经纬度
         */
        private String location;

        /**
         * 商圈名称
         */
        private String name;

        /**
         * 商圈所在区域的adcode
         */
        @JsonProperty("id")
        private String adcode;

    }

}
