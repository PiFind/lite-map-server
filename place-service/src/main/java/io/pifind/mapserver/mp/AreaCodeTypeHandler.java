package io.pifind.mapserver.mp;

public class AreaCodeTypeHandler extends IntegerListTypeHandler {

    public static final String DELIMITER = "/";

    @Override
    protected String delimiter() {
        return DELIMITER;
    }

}
