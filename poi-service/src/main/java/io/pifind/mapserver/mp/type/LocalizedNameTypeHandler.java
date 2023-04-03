package io.pifind.mapserver.mp.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 本地化命名处理器
 */
public class LocalizedNameTypeHandler implements TypeHandler<Map<Locale,String>> {

    private static final Map<String,Locale> LANGUAGE_MAP;

    private static final ObjectMapper OBJECT_MAPPER;

    private static final Comparator<String> KEY_COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    };

    static {

        // 语言映射表
        LANGUAGE_MAP = new HashMap<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            LANGUAGE_MAP.put(locale.toString().toUpperCase(),locale);
        }

        OBJECT_MAPPER = new ObjectMapper();
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Map<Locale, String> localeNameMap, JdbcType jdbcType) throws SQLException {

        // 按照顺序排序
        Map<String,String> names = new TreeMap<>(KEY_COMPARATOR);
        for (Map.Entry<Locale,String> localeNameEntry : localeNameMap.entrySet() ) {
            names.put(
                    localeNameEntry.getKey().toString(),
                    localeNameEntry.getValue()
            );
        }

        String data = null;
        try {
            data = OBJECT_MAPPER.writeValueAsString(names);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        preparedStatement.setString(i,data);
    }

    @Override
    public Map<Locale, String> getResult(ResultSet resultSet, String s) throws SQLException {
        return convert(resultSet.getString(s));
    }

    @Override
    public Map<Locale, String> getResult(ResultSet resultSet, int i) throws SQLException {
        return convert(resultSet.getString(i));
    }

    @Override
    public Map<Locale, String> getResult(CallableStatement callableStatement, int i) throws SQLException {
        return convert(callableStatement.getString(i));
    }

    private static Map<Locale, String> convert(String text) {

        if (text == null) {
            return null;
        }

        Map<Locale, String> result = new HashMap<>();
        Map<String, String> names = null;
        try {
            names = OBJECT_MAPPER.readValue(text, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<String, String> entry : names.entrySet()) {
            String language = entry.getKey().toUpperCase();
            if (LANGUAGE_MAP.containsKey(language)) {
                result.put(LANGUAGE_MAP.get(language),entry.getValue());
            }
        }

        return result;
    }

}
