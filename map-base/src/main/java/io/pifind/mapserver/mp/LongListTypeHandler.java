package io.pifind.mapserver.mp;

/**
 * Long列表类型处理
 */
public class LongListTypeHandler extends AbstractListTypeHandler<Long> {

    // 分隔符
    public static final String DELIMITER = ",";

    @Override
    protected String delimiter() {
        return DELIMITER;
    }

    @Override
    protected String toString(Long number) {
        return number.toString();
    }

    @Override
    protected Long convert(String item) {
        return Long.parseLong(item);
    }

}
