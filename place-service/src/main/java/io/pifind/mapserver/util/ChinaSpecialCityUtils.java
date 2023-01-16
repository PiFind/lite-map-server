package io.pifind.mapserver.util;

import java.util.HashMap;
import java.util.Map;

public class ChinaSpecialCityUtils {

    private static final Map<String,Long> specialCityMap;

    static {
        specialCityMap = new HashMap<>();
        specialCityMap.put("HK",14533L); // 香港
        specialCityMap.put("MO",14534L); // 澳门
        specialCityMap.put("TW",14532L); // 台湾
    }

    public static boolean check(String code) {
        return specialCityMap.containsKey(code);
    }

    public static Long getAdministrativeAreaId(String code) {
        return specialCityMap.get(code);
    }


}
