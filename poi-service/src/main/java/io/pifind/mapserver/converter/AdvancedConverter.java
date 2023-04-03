package io.pifind.mapserver.converter;

import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;

public interface AdvancedConverter<S,T> extends Converter<S,T> {

    default List<T> convert(List<S> list) {
        List<T> result = new ArrayList<>();
        for (S s : list) {
            result.add(convert(s));
        }
        return result;
    }

}
