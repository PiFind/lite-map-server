package io.pifind.mapserver.mp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractJsonListTypeHandler<T> extends BaseTypeHandler<List<T>> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CollectionType listType ;

    protected AbstractJsonListTypeHandler() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {

            // 获取泛型的类型名
            String classname = null;
            Type t = getClass().getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                for (Type type : ((ParameterizedType) t).getActualTypeArguments()) {
                    classname = type.getTypeName();
                    break;
                }
            }

            // 生成列表转换类型
            listType = objectMapper.getTypeFactory().constructCollectionType (
                    ArrayList.class,
                    Class.forName(classname)
            );

            log.info("List<{}> type created",classname);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<T> strings, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setString(i,objectMapper.writeValueAsString(strings));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return convert(resultSet.getString(s));
    }

    @Override
    public List<T> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return convert(resultSet.getString(i));
    }

    @Override
    public List<T> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return convert(callableStatement.getString(i));
    }

    private List<T> convert(String str) {
        try {
            return objectMapper.readValue(str, listType);
        } catch (JsonProcessingException e) {
            log.error("list convert error : {}",e.getMessage());
            return null;
        }
    }

}