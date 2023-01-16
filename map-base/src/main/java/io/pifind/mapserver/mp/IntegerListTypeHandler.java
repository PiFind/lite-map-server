package io.pifind.mapserver.mp;

public class IntegerListTypeHandler extends AbstractListTypeHandler<Integer> {

    // 分隔符
    public static final String DELIMITER = ",";

    @Override
    protected String delimiter() {
        return DELIMITER;
    }

    @Override
    protected String toString(Integer number) {
        return number.toString();
    }

    @Override
    protected Integer convert(String item) {
        return Integer.valueOf(item);
    }

}
