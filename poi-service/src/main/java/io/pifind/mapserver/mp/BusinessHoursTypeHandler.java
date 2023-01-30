package io.pifind.mapserver.mp;

import io.pifind.mapserver.model.po.component.TimeIntervalPO;
import io.pifind.mapserver.model.po.component.TimeIntervalSet;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 营业时间处理器
 */
public class BusinessHoursTypeHandler implements TypeHandler<TimeIntervalSet> {

    public static final Pattern PATTERN = Pattern.compile("\\(\\s*[0-9]+\\s*,\\s*[0-9]+\\s*\\)");

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, TimeIntervalSet timeIntervalSet, JdbcType jdbcType) throws SQLException {

        StringBuilder param = new StringBuilder() ;

        // 添加存储内容
        // (s1,e1)(s2,e2)...(sn,en)
        for (TimeIntervalPO timeInterval : timeIntervalSet) {

            double start = timeInterval.getStart();
            double end = timeInterval.getEnd();

            // 如果不是正确的时间段，那么就报错
            if (! (checkTime(start) && checkTime(end)) ) {
                throw new SQLException(String.format("Wrong time parameter : start=%f,end=%f",start,end));
            }

            // 格式化字符串
            param.append("(")
                    .append(String.format("%.1f",timeInterval.getStart()))
                    .append(",")
                    .append(String.format("%.1f",timeInterval.getStart()))
                    .append(")");
        }

        preparedStatement.setString(i,param.toString());
    }

    @Override
    public TimeIntervalSet getResult(ResultSet resultSet, String s) throws SQLException {
        String data = resultSet.getString(s);
        return convert(data);
    }

    @Override
    public TimeIntervalSet getResult(ResultSet resultSet, int i) throws SQLException {
        String data = resultSet.getString(i);
        return convert(data);
    }

    @Override
    public TimeIntervalSet getResult(CallableStatement callableStatement, int i) throws SQLException {
        String data = callableStatement.getString(i);
        return convert(data);
    }

    /**
     * 将格式化的字符串转换为时间区间表
     * @param formattedString 格式化的字符串
     * @return 时间区间表
     */
    private TimeIntervalSet convert(String formattedString) {
        TimeIntervalSet set = new TimeIntervalSet();
        Matcher matcher = PATTERN.matcher(formattedString);
        while(matcher.find()) {
            String p = matcher.group();
            String[] numbers = p.replaceAll("\\(","")
                    .replaceAll("\\)","")
                    .replaceAll("\\s","")
                    .split(",");

            if (numbers.length >= 2) {
                TimeIntervalPO timeInterval = new TimeIntervalPO();
                timeInterval.setStart(Double.parseDouble(numbers[0]));
                timeInterval.setEnd(Double.parseDouble(numbers[1]));
                set.add(timeInterval);
            }
        }
        return set;
    }

    private boolean checkTime(double n) {
        return 0.0 <= n && n <= 24.0;
    }

}
