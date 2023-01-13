package io.pifind.mapserver.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPv4Utils {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final Pattern IPv4_PATTERN;

    static {
        String lower = "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])"; // 0-255的数字
        String regex = lower + "(\\." + lower + "){3}";
        IPv4_PATTERN = Pattern.compile(regex);
    }

    /**
     * IPv4字符串地址转换为int类型数字
     * @param ipv4Address IPv4地址字符串
     * @return 返回值类型为 int
     * <ul>
     *     <li><b>若 {@code ipv4Address} 符合IPv4定义</b> - 返IP对应的回数字</li>
     *     <li><b>若 {@code ipv4Address} 不符合IPv4定义</b> - 抛出 {@link RuntimeException} 异常</li>
     * </ul>
     */
    public static int stringToInt(String ipv4Address) {
        // 判断是否是ip格式的
        if (!isIPv4Address(ipv4Address))
            throw new RuntimeException("Invalid ip address");

        // 匹配数字
        Matcher matcher = NUMBER_PATTERN.matcher(ipv4Address);
        int result = 0;
        int counter = 0;
        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group());
            result = (value << 8 * (3 - counter++)) | result;
        }
        return result;
    }

    /**
     * 判断是否为ipv4地址
     * @param ipv4Address IPv4地址字符串
     * @return 返回值类型为 {@link Boolean}
     * <ul>
     *     <li><b>是IPv4地址</b> - 返回 {@code true}</li>
     *     <li><b>不是IPv4地址</b> - 返回 {@code false}</li>
     * </ul>
     */
    public static boolean isIPv4Address(String ipv4Address) {
        Matcher matcher = IPv4_PATTERN.matcher(ipv4Address);
        return matcher.matches();
    }

    /**
     * 将int数字转换成ipv4地址
     * @param ip 数值类型的IPv4地址
     * @return IPv4地址字符串
     */
    public static String intToIPv4(int ip) {
        StringBuilder sb = new StringBuilder();
        int num = 0;
        boolean needPoint = false;
        for (int i = 0; i < 4; i++) {
            if (needPoint) {
                sb.append('.');
            }
            needPoint = true;
            int offset = 8 * (3 - i);
            num = (ip >> offset) & 0xff;
            sb.append(num);
        }
        return sb.toString();
    }

}
