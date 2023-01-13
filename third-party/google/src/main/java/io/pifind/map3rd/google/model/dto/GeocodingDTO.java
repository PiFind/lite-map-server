package io.pifind.map3rd.google.model.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 地理编码/反向地理编码返回值实体类
 */
@Data
public class GeocodingDTO {

    /**
     * 数组表示返回结果的类型。此数组包含一组零个或多个标记，用于标识结果中返回的地图项类型。
     * 例如，“芝加哥”的地理编码会返回“市行政区”，表示“芝加哥”是一个城市；同时返回“政治”，
     * 表示它是一个政治实体。
     */
    private String[] types;

    /**
     * 是一个字符串，其中包含此位置直观易懂的地址。
     * <p>
     *     此地址通常相当于邮政地址。请注意，由于许可限制，某些国家/地区（例如英国）不允许发布真实的邮政地址。
     * </p>
     * <p>
     *     设有格式的地址在逻辑上包含一个或多个地址组成部分。例如，地址“111 8th Avenue, New York, NY”
     *     包含以下组成部分：“111”（门牌号）、“8th Avenue”（道路名称）、“New York”（城市）和“NY”（美国州名）。
     * </p>
     * <p>
     *     请勿以程序化方式解析设有格式的地址。您应改用单独的地址组成部分，API 响应除了包含设有格式的地址字段外，
     *     还包含这些组成部分。
     * </p>
     */
    @JSONField(name = "formatted_address")
    private String formattedAddress;

    /**
     * 地址组件列表
     * <p></p>
     * 注意事项如下：
     * <ul>
     *     <li>
     *         地址组成部分的数组包含的组成部分可能多于 {@code formatted_address}。
     *     </li>
     *     <li>
     *         除了 {@code formatted_address} 中包含的政治实体之外，数组不一定会纳入包含地址的所有
     *         政治实体。若要检索包含特定地址的所有政治实体，您应使用反向地理编码，并将地址的纬度/经度作
     *         为参数传递给请求。
     *     </li>
     *     <li>
     *         两次请求之间的响应格式不一定相同。特别是，{@code address_components} 的数量因所请求
     *         的地址而异，对于同一个地址，数量也可能会随着时间推移而发生变化。组成部分在数组中的位置可能
     *         发生变化。组成部分的类型也可能发生变化。后续响应中可能缺少特定组成部分。
     *     </li>
     *     <li>
     *         要处理组件数组，应解析响应，并通过表达式选择合适的值。请参阅
     *         <a href="https://developers.google.com/maps/documentation/geocoding/web-service-best-practices#Parsing">解析响应指南</a>。
     *     </li>
     * </ul>
     */
    @JSONField(name = "address_components")
    private AddressComponent[] addressComponents;

    @Data
    public static class AddressComponent {

        /**
         * 表示地址组成部分的类型。请参阅支持的类型列表。
         */
        private List<String> types;

        /**
         * 是地理编码器返回的地址组成部分的完整文本说明或名称。
         */
        @JSONField(name = "long_name")
        private String lastName;

        /**
         * 是地址组成部分的缩写文本名称（如果有）。例如，阿拉斯加州的地址组成部分可能包含
         * {@code long_name} “Alaska”和 {@code short_name}“AK”（使用 2 个字母
         * 的邮编缩写）。
         */
        @JSONField(name = "short_name")
        private String shortName;

    }

    /**
     * 表示邮政编码中包含的所有市行政区。仅当结果是包含多个市行政区的邮政编码时，此字段才会显示。
     */
    @JSONField(name = "postcode_localities")
    private List<String> postcodeLocalities;

    private Geometry geometry;

    @Data
    public static class Geometry {

        /**
         * 包含经过地理编码的纬度和经度值。对于正常的地址查询，此字段通常是最重要的。
         */
        private WGS84CoordinateDTO location;

        /**
         * 存储有关指定位置的其他数据。目前支持以下值：
         * <ul>
         *     <li>
         *         "{@code ROOFTOP}" 表示返回的结果是一个精确的地理编码，我们有精确到街道地址的位置信息。
         *     </li>
         *     <li>
         *         "{@code RANGE_INTERPOLATED}" 表示返回的结果反映了两个精确点（例如十字路口）之间用内
         *         插法计算得到的近似值（通常在道路上）。当某个街道地址的屋顶地理编码不可用时，通常会返回插值
         *         结果。
         *     </li>
         *     <li>
         *         "{@code GEOMETRIC_CENTER}" 表示返回的结果是多段线（例如街道）或多边形（例如区域）等
         *         结果的几何图形中心。
         *     </li>
         *     <li>
         *         "{@code APPROXIMATE}" 表示返回的结果是近似值。
         *     </li>
         * </ul>
         */
        @JSONField(name = "location_type")
        private String locationType;

        /**
         * 包含用于显示返回结果的建议视口，指定为两个纬度/经度值，分别定义视口边界框的
         * {@code southwest} 和 {@code northeast} 角。通常，视口会在向用户显示
         * 结果时对结果进行框架处理。
         */
        private RegionDTO viewport;

        /**
         * （可选返回）存储可完全包含返回结果的边界框。请注意，这些边界可能与推荐的视口不匹配。
         * （例如，旧金山包含法拉隆岛，理论上它是这个城市的一部分，但不应该在视口中返回）。
         */
        private RegionDTO bounds;
    }

    /**
     * （请参阅<a href="https://en.wikipedia.org/wiki/Open_Location_Code">Open Location Code</a>
     * 和<a href="https://plus.codes/">Plus Code</a>）是经过编码的位置引用，衍生自纬度和经度坐标，表示面
     * 积：1/8000 度、1/8000 度（赤道约 14 米 x 14 米）。Plus Codes 可用于取代位于虚假地点的街道地址，例如
     * 无编号的建筑物或无名街道。
     * <p>Plus Code 的格式包括全局代码和混合代码：</p>
     * <ul>
     *     <li>
     *         {@code global_code} 是 4 个字符的区号和 6 个字符或更长的本地代码 (849VCWC8+R9)。
     *     </li>
     *     <li>
     *         {@code compound_code} 是至少包含 6 个字符的区域代码，具有明确的位置信息（CWC8+R9, Mountain View, CA, USA）。
     *         请勿以程序化方式解析此内容。
     *     </li>
     * </ul>
     * 通常情况下，系统会返回全局代码和混合代码。但是，如果结果是在偏远位置（例如海洋或沙漠），系统可能只会返回全局代码。
     */
    @JSONField(name ="plus_code")
    private String plusCode;

    /**
     * 表示地理编码器无法返回与原始请求完全匹配的结果，但能够匹配所请求地址的一部分。建议您检查一下原始请求中是否存在拼写错误
     * 和/或地址不完整的情况。
     * <p>
     * 对于请求中所传递的市行政区内不存在的街道地址，最常发生部分匹配的情况。当请求与同一市行政区中的两个或更多位置相匹配时，
     * 也可能会返回部分匹配。例如，无论是 Henry Street 还是 Henrietta Street，“Hillpar St, Bristol, UK”都会返回
     * 部分匹配。请注意，如果请求中包含拼写错误的地址组成部分，地理编码服务可能会推荐备选地址。通过这种方式触发的建议也将标记
     * 为部分匹配。
     * </p>
     */
    @JSONField(name = "partial_match ")
    private String partialMatch;

    /**
     * 唯一标识符，可以与其他 Google API 搭配使用。
     * 例如，您可以在 <a href="https://developers.google.com/maps/documentation/places/web-service/details">Places API</a>
     * 请求中使用 {@code place_id} 获取本地商家的详细信息，例如电话号码、营业时间、用户评价等。
     * 请参阅<a href="https://developers.google.com/maps/documentation/places/web-service/place-id">地点 ID 概览</a>。
     */
    private String placeId;

}
