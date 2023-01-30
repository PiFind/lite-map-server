package io.pifind.mapserver.mp;

import io.pifind.mapserver.model.constant.WeekEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 营业日处理器
 */
public class BusinessDayTypeHandler implements TypeHandler<Map<WeekEnum,Boolean>> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Map<WeekEnum, Boolean> weekMap, JdbcType jdbcType) throws SQLException {
        int param = 0;
        for (WeekEnum day : weekMap.keySet()) {
            if (!weekMap.get(day)) {
                continue;
            }
            param = param | day.code();
        }
        preparedStatement.setInt(i,param);
    }

    @Override
    public Map<WeekEnum, Boolean> getResult(ResultSet resultSet, String s) throws SQLException {
        Integer data =  resultSet.getInt(s);
        return convert(data);
    }

    @Override
    public Map<WeekEnum, Boolean> getResult(ResultSet resultSet, int i) throws SQLException {
        Integer data =  resultSet.getInt(i);
        return convert(data);
    }

    @Override
    public Map<WeekEnum, Boolean> getResult(CallableStatement callableStatement, int i) throws SQLException {
        Integer data =  callableStatement.getInt(i);
        return convert(data);
    }

    /**
     * 将整型数据转换为营业日映射表数据
     * @param data 整型数据
     * @return 营业日映射表
     */
    private Map<WeekEnum, Boolean> convert(Integer data) {

        if (data == null) {
            return null;
        }

        Map<WeekEnum, Boolean> result = new HashMap<>();
        for (WeekEnum day : WeekEnum.values()) {
            result.put(day,(data&day.code()) != 0);
        }

        return result;
    }

}
