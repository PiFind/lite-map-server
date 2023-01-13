package io.pifind.map3rd.google.model.qo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.pifind.common.annotation.QueryObject;
import lombok.Data;

import static io.pifind.map3rd.google.api.GoogleGeocodingAPI.REVERSE_GEOCODING_API;

/**
 * 反向地理编码查询实体类
 */
@Data
@QueryObject(REVERSE_GEOCODING_API)
public class ReverseGeocodingQO {

    /**
     * 用于指定希望获取的距离最近且直观易懂的地址的纬度和经度值。
     */
    private String latlng;

    /**
     * 返回结果时所使用的语言。
     * <ul>
     *     <li>
     *         请参阅<a href="https://developers.google.com/maps/faq#languagesupport">支持的语言列表</a>。
     *         Google 会经常更新支持的语言，因此该列表可能并不详尽。
     *     </li>
     *     <li>
     *         如果未提供 {@code language}，地理编码器会尝试使用 {@code Accept-Language} 标头中指定的首选
     *         语言，或发出请求的网域的母语。
     *     </li>
     *     <li>
     *         地理编码器会尽力提供对用户和本地人都而言可读的街道地址。为了实现这一目标，它会以当地语言返回街道地址，
     *         如有必要，系统会将其转写为用户可以阅读的脚本，同时注意首选语言。所有其他地址都会以首选语言返回。地址
     *         部分均以同一语言（从第一个组成部分中选择）返回。
     *     </li>
     *     <li>
     *         如果首选语言中没有相应名称，地理编码器会使用最接近的匹配项。
     *     </li>
     *     <li>
     *         首选语言对 API 选择返回的结果集及其返回顺序只有很小的影响。地理编码器会根据语言以不同方式解析缩写，
     *         例如街道类型的缩写，或者可能在一种语言中有效但在另一种语言中无效的同义词。例如，在匈牙利语中，utca
     *         和 tér 是街道和方形的同义词。
     *     </li>
     * </ul>
     */
    private String language;

    /**
     * 一个或多个地址类型的过滤条件，以竖线字符 (|) 分隔。如果参数包含多个地址类型，则 API 将返回与任意类型匹配的所有地址。
     * 关于处理的说明：result_type 参数不会限制搜索特定的地址类型。而是 result_type 充当搜索后过滤器：API 会提取指定
     * latlng 的所有结果，然后舍弃与指定地址类型不匹配的结果。支持以下值：
     *
     * <ul>
     *     <li>
     *         {@code street_address} 表示精确的街道地址。
     *     </li>
     *     <li>
     *         {@code route} 表示已命名的路线（例如“US 101”）。
     *     </li>
     *     <li>
     *         {@code intersection} 表示主要交叉路口，通常是两条主要道路的交叉路口。
     *     </li>
     *     <li>
     *         {@code political} 表示政治实体。这种类型通常表示某些行政管理区的多边形区域。
     *     </li>
     *     <li>
     *         {@code country} 表示国家政治实体，通常列在地理编码器所返回结果的最前面。
     *     </li>
     *     <li>
     *         {@code administrative_area_level_1} 表示国家/地区级别以下的一级行政实体。在美国，这类行政级别是指州。
     *         并不是所有国家都设有这类行政级别。在大多数情况下，{@code administrative_area_level_1} 简称可高度匹配
     *         <b>ISO 3166-2</b> 行政区划以及其他广为传播的列表；不过，我们无法做出保证，因为我们的地理编码结果基于各种
     *         信号和位置数据。
     *     </li>
     *     <li>
     *         {@code administrative_area_level_2} 表示国家/地区级别以下的二级行政实体。在美国，这类行政级别是指县。
     *         并不是所有国家都设有这类行政级别。
     *     </li>
     *     <li>
     *         {@code administrative_area_level_3} 表示国家/地区级别以下的三级行政实体。此类型表示较小的行政区划单位。
     *         并不是所有国家都设有这类行政级别。
     *     </li>
     *     <li>
     *         {@code administrative_area_level_4} 表示国家/地区级别以下的四级行政实体。此类型表示较小的行政区划单位。
     *         并不是所有国家都设有这类行政级别。
     *     </li>
     *     <li>
     *         {@code administrative_area_level_5} 表示国家/地区级别以下的五级行政实体。此类型表示较小的行政区划单位。
     *         并不是所有国家都设有这类行政级别。
     *     </li>
     *     <li>
     *         {@code administrative_area_level_6} 表示国家/地区级别以下的六级行政实体。此类型表示较小的行政区划单位。
     *         并不是所有国家都设有这类行政级别。
     *     </li>
     *     <li>
     *         {@code administrative_area_level_7} 表示国家/地区级别以下的七级行政实体。此类型表示较小的行政区划单位。
     *         并不是所有国家都设有这类行政级别。
     *     </li>
     *     <li>
     *         {@code colloquial_area} 表示实体的常用替代名称。
     *     </li>
     *     <li>
     *         {@code locality} 表示合并的城市或城镇政治实体。
     *     </li>
     *     <li>
     *         {@code sublocality} 表示市行政区以下的一级行政实体。某些位置可能会收到以下任一类型：从 {@code sublocality_level_1}
     *         到 {@code sublocality_level_5}。每个子级市行政区级别都是一个行政实体。数字越大表示地理区域越小。
     *     </li>
     *     <li>
     *         {@code neighborhood} 表示已命名的街区。
     *     </li>
     *     <li>
     *         {@code premise} 表示已命名的位置，通常是具有常见名称的一栋或一群建筑物。
     *     </li>
     *     <li>
     *         {@code subpremise} 表示已命名位置以下的一级实体，通常是同名建筑群中的单个建筑物。
     *     </li>
     *     <li>
     *         {@code plus_code} 表示经过编码的位置引用，衍生自纬度和经度。Plus Codes 可用于取代位于虚假地点的街道地址，
     *         例如无编号的建筑物或无名街道。如需了解详情，请参阅 <a href="https://plus.codes">https://plus.codes</a>。
     *     </li>
     *     <li>
     *         {@code postal_code} 表示邮政编码，用于国家/地区内的地址邮寄。
     *     </li>
     *     <li>
     *         {@code natural_feature} 表示某个明显的自然地貌。
     *     </li>
     *     <li>
     *         {@code airport} 表示机场。
     *     </li>
     *     <li>
     *         {@code park} 表示已命名的公园。
     *     </li>
     *     <li>
     *         {@code point_of_interest} 表示已命名的地图注点。通常情况下，这些“地图注点”是当地的著名实体，无法轻易归入其他类别，
     *         例如“帝国大厦”或“埃菲尔铁塔”。
     *     </li>
     * </ul>
     */
    @JSONField(name = "result_type")
    private String resultType;

    /**
     *  一个或多个位置类型的过滤条件，用竖线 (|) 分隔。如果参数包含多个位置类型，则 API 将返回与任意类型匹配的所有地址。
     *  关于处理的说明：{@code location_type} 参数不会限制搜索特定的位置类型。而是 {@code location_type} 充当
     *  搜索后过滤器：API 会提取指定 {@code latlng} 的所有结果，然后舍弃与指定位置类型不匹配的结果。支持以下值：
     *  <ul>
     *      <li>
     *         {@code "ROOFTOP"} 仅返回 Google 的位置信息（精确到街道地址精确度）的地址。
     *      </li>
     *      <li>
     *          {@code "RANGE_INTERPOLATED"} 仅返回反映两个精确点（例如十字路口）之间用插值得出的近似地址（通常在道路上）
     *          的地址。插值范围通常表示某个街道地址的屋顶地理编码不可用。
     *      </li>
     *      <li>
     *          {@code "GEOMETRIC_CENTER"} 仅返回营业地点的几何图形中心，如多段线（例如街道）或多边形（区域）。
     *      </li>
     *      <li>
     *          {@code "APPROXIMATE"} 仅返回特征为近似地址的地址。
     *      </li>
     *  </ul>
     * 如果同时存在 {@code result_type} 和 {@code location_type} 过滤条件，则 API 仅返回同时与 {@code result_type}
     * 和 {@code location_type}值匹配的结果。如果所有过滤条件值都不可接受，则 API 会返回 {@code ZERO_RESULTS}。
     */
    @JSONField(name = "location_type")
    private String locationType;

}
