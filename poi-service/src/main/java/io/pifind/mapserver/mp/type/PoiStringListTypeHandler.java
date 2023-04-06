package io.pifind.mapserver.mp.type;

import io.pifind.mapserver.mp.StringListTypeHandler;

/**
 * POI字符串列表处理器
 */
public class PoiStringListTypeHandler extends StringListTypeHandler {

    // 分隔符
    public static final String DELIMITER = "|";

    @Override
    protected String delimiter() {
        return DELIMITER;
    }
    
}
