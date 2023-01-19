package io.pifind.map3rd.amap.model.qo;

import io.pifind.common.annotation.QueryObject;
import io.pifind.map3rd.amap.support.AmapGeocodingAPI;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 地理编码查询实体类
 * <p>
 * <a href="https://lbs.amap.com/api/webservice/guide/api/georegeo#geo">
 * 高德地图开放平台-地理编码
 * </a>
 * </p>
 */
@Data
@AllArgsConstructor
@QueryObject(AmapGeocodingAPI.GEOCODING_API)
public class GeocodingQO {

    /**
     * <b>[必填]</b> - 结构化地址信息
     * <p>
     * 规则遵循：国家、省份、城市、区县、城镇、乡村、街道、门牌号码、屋邨、大厦，如：北京市朝阳区阜通东大街6号。
     * </p>
     */
    private String address;

    /**
     * <b>[必填]</b> - 指定查询的城市
     * <p>
     *     可选输入内容包括：指定城市的中文（如北京）、指定城市的中文全拼（beijing）、
     *     citycode（010）、adcode（110000），不支持县级市。当指定城市查询内容为空时，
     *     会进行全国范围内的地址转换检索。 adcode信息可参考城市编码表获取
     * </p>
     */
    private String city;

    /**
     * <b>[可选]</b> - 返回数据格式类型
     * <p>默认：JSON</p>
     * <p>
     *     可选输入内容包括：JSON，XML。设置 JSON 返回结果数据将会以JSON结构构成；
     *     如果设置 XML 返回结果数据将以 XML 结构构成。
     * </p>
     */
    private String output;

    /**
     * <b>[可选]</b> - 回调函数
     * <p>
     *     callback 值是用户定义的函数名称，此参数只在 output 参数设置为 JSON 时有效。
     * </p>
     */
    private String callback;

    /*
     * 下面是根据“必填”标签进行创建实体类的构造方法
     */

    /**
     * 根据地理地址地理编码查询实体类
     * @param address 地理地址
     */
    public GeocodingQO(String address) {
        this.address = address;
    }

}
