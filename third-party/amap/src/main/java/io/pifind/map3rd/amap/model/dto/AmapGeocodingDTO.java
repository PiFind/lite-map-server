package io.pifind.map3rd.amap.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 高德地图地理编码返回值实体类对象
 */
@Data
public class AmapGeocodingDTO {

    /**
     * 返回结果数目
     */
    private Integer count;

    /**
     * 地理编码信息列表
     */
    private List<Geocode> geocodes;

    @Data
    public static class Geocode {

        /**
         * 国家
         * <p>国内地址默认返回中国</p>
         */
        private String country;

        /**
         * 地址所在的省份名
         * <p>例如：北京市。此处需要注意的是，中国的四大直辖市也算作省级单位。</p>
         */
        private String province;

        /**
         * 地址所在的城市名
         * <p>例如：北京市</p>
         */
        private String city;

        /**
         * 城市编码
         * <p>例如：010</p>
         */
        @JsonProperty("citycode")
        private Integer cityCode;

        /**
         * 地址所在的区
         * <p>例如：朝阳区</p>
         */
        private String district;

        /**
         * 街道
         * <p>例如：阜通东大街</p>
         */
        private String street;

        /**
         * 门牌
         * <p>例如：6号</p>
         */
        private String number;

        /**
         * 区域编码
         * <p>例如：110101</p>
         */
        private String adcode;

        /**
         * 坐标点
         * <p>经度，纬度</p>
         */
        private String location;

        /**
         * 匹配级别
         * <table class="md-table  " data-meta="%7B%22width%22%3A%22100%25%22%7D">
         *   <tbody>
         *     <tr>
         *       <th colspan="2">
         *         <p>匹配级别</p>
         *       </th>
         *       <th>
         *         <p>示例说明</p>
         *       </th>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>国家</p>
         *       </td>
         *       <td>
         *         <p>中国</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>省</p>
         *       </td>
         *       <td>
         *         <p>河北省、北京市</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>市</p>
         *       </td>
         *       <td>
         *         <p>宁波市</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>区县</p>
         *       </td>
         *       <td>
         *         <p>北京市朝阳区</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>开发区</p>
         *       </td>
         *       <td>
         *         <p>亦庄经济开发区</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>乡镇</p>
         *       </td>
         *       <td>
         *         <p>回龙观镇</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p data-spm-anchor-id="0.0.0.i88.3b3ad32fU7lPzc">村庄</p>
         *       </td>
         *       <td>
         *         <p>三元村</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>热点商圈</p>
         *       </td>
         *       <td>
         *         <p>上海市黄浦区老西门</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>兴趣点</p>
         *       </td>
         *       <td>
         *         <p>北京市朝阳区奥林匹克公园(南门)</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>门牌号</p>
         *       </td>
         *       <td>
         *         <p>朝阳区阜通东大街6号</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>单元号</p>
         *       </td>
         *       <td>
         *         <p>望京西园四区5号楼2单元</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>道路</p>
         *       </td>
         *       <td>
         *         <p>北京市朝阳区阜通东大街</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>道路交叉路口</p>
         *       </td>
         *       <td>
         *         <p>北四环西路辅路/善缘街</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>公交站台、地铁站</p>
         *       </td>
         *       <td>
         *         <p>海淀黄庄站A1西北口</p>
         *       </td>
         *     </tr>
         *     <tr>
         *       <td colspan="2">
         *         <p>未知</p>
         *       </td>
         *       <td>
         *         <p>未确认级别的POI</p>
         *       </td>
         *     </tr>
         *   </tbody>
         * </table>
         */
        private String level;

    }

}
