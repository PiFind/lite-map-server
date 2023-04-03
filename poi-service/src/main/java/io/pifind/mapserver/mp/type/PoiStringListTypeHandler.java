package io.pifind.mapserver.mp.type;

import io.pifind.mapserver.mp.StringListTypeHandler;

public class PoiStringListTypeHandler extends StringListTypeHandler {

    // 分隔符
    public static final String DELIMITER = "|";

    @Override
    protected String delimiter() {
        return DELIMITER;
    }
    
}
