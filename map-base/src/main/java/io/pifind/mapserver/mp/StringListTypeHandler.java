package io.pifind.mapserver.mp;

/**
 * 字符串列表类型处理
 */
public class StringListTypeHandler extends AbstractListTypeHandler<String> {

    // 分隔符
    public static final String DELIMITER = ",";

    @Override
    protected String delimiter() {
        return DELIMITER;
    }

    @Override
    protected String toString(String s) {
        return s;
    }

    @Override
    protected String convert(String item) {
        return item;
    }

}
