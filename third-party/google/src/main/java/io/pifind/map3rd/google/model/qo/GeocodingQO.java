package io.pifind.map3rd.google.model.qo;

import io.pifind.common.annotation.QueryObject;
import lombok.Data;

import static io.pifind.map3rd.google.support.GoogleGeocodingAPI.GEOCODING_API;

/**
 * 地理编码查询实体类
 */
@Data
@QueryObject(GEOCODING_API)
public class GeocodingQO {

    /**
     * <b>[必填]</b> - 地理地址
     *
     * <p>
     * 要进行地理编码的街道地址或 <a href="https://plus.codes/">Plus Code</a> 。
     * 请按照相关国家/地区的邮政服务所使用的地址格式指定地址。应避免使用额外的地址元素，
     * 例如商家名称和单元、房间号或楼层号。街道地址元素应以空格分隔（此处显示为网址转义为
     * {@code %20}）：
     * </p>
     * <p>
     *     {@code address=24%20Sussex%20Drive%20Ottawa%20ON}
     * </p>
     *
     * 按如下格式设置 Plus 代码（加号经网址转义为 {@code %2B} ，空格经网址转义为 {@code %20}）：
     * <ul>
     *     <li>
     *         <b>全局代码</b>是包含 4 个字符的区号和至少包含 6 个字符的当地代码
     *         （{@code 849VCWC8+R9} 为 {@code 849VCWC8%2BR9}）。
     *     </li>
     *     <li>
     *         <b>组合代码</b>是至少包含 6 个字符的区域代码，具有明确的位置信息
     *         （{@code CWC8+R9 Mountain View, CA, USA} 为
     *         {@code CWC8%2BR9%20Mountain%20View%20CA%20USA}）。
     *     </li>
     * </ul>
     */
    private String address;

    /**
     * <b>[可选]</b> - 视口边界
     * <p>
     * 视口的边界框，可在其中更明显地对地理编码结果进行偏向。此参数只会影响，而不会完全限制地理编码器中的结果。
     * （如需了解详情，请参阅下文的<a href="https://developers.google.com/maps/documentation/geocoding/requests-geocoding#Viewports">视口自定义调整</a>。）
     * </p>
     */
    private String bounds;

    /**
     * <b>[可选]</b> - 地理地址返回结果时所使用的语言。
     * <p>默认：使用 {@code Accept-Language} 标头中指定的首选语言</p>
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
     * <b>[可选]</b> - 地区代码，指定为 ccTLD（“顶级域名”）双字符值。此参数只会影响，而不会完全限制地理编码器中的结果。（
     * 如需了解详情，请参阅下文的
     * <a href="https://developers.google.com/maps/documentation/geocoding/requests-geocoding#RegionCodes">区域自定义调整</a>）。
     */
    private String region;

    /**
     *  <b>[可选]</b> - 过滤组件
     *  <p>
     *  包含以竖线 (|) 分隔元素的组件过滤条件。如果提供 {@code address}，则组件过滤条件也可用作可选参数。
     *  组件过滤条件中的每个元素都包含一个 {@code component:value} 对，并完全限制了地理编码器中的结果。
     *  如需了解详情，请参阅
     *  <a href="https://developers.google.com/maps/documentation/geocoding/requests-geocoding#component-filtering">组件过滤</a>。
     *  </p>
     */
    private String components;

}
