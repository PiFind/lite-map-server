package io.pifind.mapserver.redis;

import io.pifind.common.util.StringFormatUtils;

/**
 * 格式化字符串键生成器
 */
public class FormattedStringKeyGenerator implements IStringKeyGenerator<Object>{

    private final String formatExpression;

    private final String regularExpression;

    private FormattedStringKeyGenerator(String formatExpression) {
        this.formatExpression = formatExpression;
        this.regularExpression = StringFormatUtils.createRegularExpressionByFormatString(formatExpression);
    }

    private FormattedStringKeyGenerator(String formatExpression,String regularExpression) {
        this.formatExpression = formatExpression;
        this.regularExpression = regularExpression;
    }

    @Override
    public String generate(Object... objects) {
        return String.format(formatExpression,objects);
    }

    @Override
    public boolean isMatched(String s) {
        return !s.matches(regularExpression);
    }

    /**
     * 创建一个格式化字符串键生成器
     * @param formatExpression 格式化表达式
     * @return 格式化字符串键生成器实体
     */
    public static FormattedStringKeyGenerator create(String formatExpression) {
        return new FormattedStringKeyGenerator(formatExpression);
    }

    /**
     * 创建一个格式化字符串键生成器
     * @param formatExpression 格式化表达式
     * @return 格式化字符串键生成器实体
     */
    public static FormattedStringKeyGenerator create(String formatExpression,String regularExpression) {
        return new FormattedStringKeyGenerator(formatExpression,regularExpression);
    }

}
