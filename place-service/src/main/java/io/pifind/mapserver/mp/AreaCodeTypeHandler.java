package io.pifind.mapserver.mp;

public class AreaCodeTypeHandler extends AbstractListTypeHandler<Long> {

    public static final String DELIMITER = "/";

    @Override
    protected String delimiter() {
        return DELIMITER;
    }

    @Override
    protected String toString(Long aLong) {
        return aLong.toString();
    }

    @Override
    protected Long convert(String item) {
        return Long.valueOf(item);
    }

}
