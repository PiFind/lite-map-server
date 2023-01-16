package io.pifind.mapserver.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 中国特别行政区城市工具类
 * <p>
 *     <font style="color:red;">
 *     <b>重要：</b> 由于国外政治原因，在使用定位服务及行政区划服务时，
 *     凡可能涉及到中国的区域必须使用该工具类进行检查和修改
 *     </font>
 * </p>
 */
public class ChinaSpecialCityUtils {

    private static final Map<String,Long> specialCityMap;

    static {
        specialCityMap = new HashMap<>();
        specialCityMap.put("HK",14533L); // 香港
        specialCityMap.put("MO",14534L); // 澳门
        specialCityMap.put("TW",14532L); // 台湾
    }

    /**
     * 检查代码是否为中国的特别行政区码
     * @param code 代码
     * @return 返回的类型为 boolean
     * <lu>
     *     <li><b>是中国特别行政区</b> - 返回 {@code true}</li>
     *     <li><b>不是中国特别行政区</b> - 返回 {@code false}</li>
     * </lu>
     */
    public static boolean check(String code) {
        return specialCityMap.containsKey(code);
    }

    /**
     * 根据代码返回中国行政区ID
     * @param code 代码
     * @return 行政区ID
     */
    public static Long getAdministrativeAreaId(String code) {
        return specialCityMap.get(code);
    }


}
