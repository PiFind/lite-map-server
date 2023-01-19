package io.pifind.map3rd.amap.model.qo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.pifind.common.annotation.QueryObject;
import io.pifind.map3rd.amap.support.AmapGeocodingAPI;
import lombok.Data;

@Data
@QueryObject(AmapGeocodingAPI.REVERSE_GEOCODING_API)
public class ReverseGeocodingQO {

    /**
     * <b>[必填]</b> - 经纬度坐标
     * 传入内容规则：经度在前，纬度在后，经纬度间以“,”分割，经纬度小数点后不要超过 6 位。
     */
    private String location;

    /**
     * <b>[可选]</b> - 返回附近POI类型
     * <p>以下内容需要 extensions 参数为 all 时才生效。</p>
     * 逆地理编码在进行坐标解析之后不仅可以返回地址描述，也可以返回经纬度附近符合限定要求的POI内容
     * （在 extensions 字段值为 all 时才会返回POI内容）。设置 POI 类型参数相当于为上述操作限定
     * 要求。参数仅支持传入POI TYPECODE，可以传入多个POI TYPECODE，相互之间用“|”分隔。获取
     * POI TYPECODE 可以参考POI分类码表
     */
    @JSONField(name = "poitype")
    private String poiType;

    /**
     * <b>[可选]</b> - 搜索半径
     * <p>默认：1000</p>
     * <p>
     *     radius取值范围在0~3000，默认是1000。单位：米
     * </p>
     */
    private String radius;

    /**
     * <b>[可选]</b> - 返回结果控制
     * <p>默认：base</p>
     * <p>extensions 参数默认取值是 base，也就是返回基本地址信息；</p>
     * <p>
     * extensions 参数取值为 all 时会返回基本地址信息、附近 POI 内容、道路信息以及道路交叉口信息。
     * </p>
     */
    private String extensions;

    /**
     * <b>[可选]</b> - 道路等级
     * <p>以下内容需要 extensions 参数为 all 时才生效。</p>
     * 可选值：0，1
     * <ol start="0">
     *     <li>显示所有道路</li>
     *     <li>过滤非主干道路，仅输出主干道路数据 </li>
     * </ol>
     */
    @JSONField(name = "roadlevel")
    private String roadLevel;

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

    /**
     * <b>[可选]</b> - 是否优化POI返回顺序
     * <p>默认：0</p>
     * <p>以下内容需要 extensions 参数为 all 时才生效。</p>
     * homeorcorp 参数的设置可以影响召回 POI 内容的排序策略，目前提供三个可选参数：
     * <ol start="0">
     *     <li>不对召回的排序策略进行干扰</li>
     *     <li>综合大数据分析将居家相关的 POI 内容优先返回，即优化返回结果中 pois 字段的poi顺序。</li>
     *     <li>综合大数据分析将公司相关的 POI 内容优先返回，即优化返回结果中 pois 字段的poi顺序。</li>
     * </ol>
     */
    @JSONField(name = "homeorcorp")
    private Integer homeOrCorp;

}
