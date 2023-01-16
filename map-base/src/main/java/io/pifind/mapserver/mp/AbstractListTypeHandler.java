package io.pifind.mapserver.mp;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象列表类型处理器
 * @param <T> 列表中的类型
 */
public abstract class AbstractListTypeHandler<T> implements TypeHandler<List<T>> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int index, List<T> data, JdbcType jdbcType) throws SQLException {
        String param = null;
        if (data != null) {
            StringBuilder segment = new StringBuilder();
            for (int i = 0; i < data.size() ; i ++) {
                segment.append(toString(data.get(i)));
                if (i + 1 < data.size()) {
                    segment.append(delimiter());
                }
            }
            param = segment.toString();
        }
        preparedStatement.setString(index, param);
    }

    @Override
    public List<T> getResult(ResultSet resultSet, String s) throws SQLException {
        return convertList(resultSet.getString(s));
    }

    @Override
    public List<T> getResult(ResultSet resultSet, int i) throws SQLException {
        return convertList(resultSet.getString(i));
    }

    @Override
    public List<T> getResult(CallableStatement callableStatement, int i) throws SQLException {
        return convertList(callableStatement.getString(i));
    }

    private List<T> convertList(String originalData) {
        // 对原始数据进行分割
        String[] items = originalData.split(delimiter());
        // 将分割后的数据进行转换
        List<T> data = new ArrayList<>();
        for (String item : items) {
            data.add(convert(item));
        }
        return data;
    }

    /**
     * 分隔符
     * @return 分隔符
     */
    protected abstract String delimiter();

    /**
     * 将 T类型对象 转换为字符串
     * @param t T类型对象
     * @return 字符串
     */
    protected abstract String toString(T t);

    /**
     * 将字符串转换为 T类型对象
     * @param item 字符串对象
     * @return T类型对象
     */
    protected abstract T convert(String item);

}
