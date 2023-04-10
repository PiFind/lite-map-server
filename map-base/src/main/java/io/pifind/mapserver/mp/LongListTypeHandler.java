package io.pifind.mapserver.mp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Long列表类型处理
 */
@Component
public class LongListTypeHandler extends BaseTypeHandler<List<Long>> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CollectionType listType ;

    public LongListTypeHandler() {

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        // 生成列表转换类型
        listType = objectMapper.getTypeFactory().constructCollectionType (
                ArrayList.class,
                Long.class
        );

    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Long> strings, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setString(i,objectMapper.writeValueAsString(strings));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Long> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return convert(resultSet.getString(s));
    }

    @Override
    public List<Long> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return convert(resultSet.getString(i));
    }

    @Override
    public List<Long> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return convert(callableStatement.getString(i));
    }

    private List<Long> convert(String str) {
        try {
            return objectMapper.readValue(str, listType);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
