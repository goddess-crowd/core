package com.goddess.framwork.infrastructure.persistence;

import com.goddess.common.exception.AppException;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author: 子玄 Aidy.Q
 * @email: Aidy.Q@bitsun-inc.com
 * @description: 防止SQL字符串注入
 */
public class SQLFilter {
    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     * @return 过滤过字符串
     */
    public static String sqlInject(String str) {
        return sqlInject(str, null);
    }

    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     * @param escape 希望不排除的字符，例如sorter的值，希望不排除逗号
     * @return 过滤过字符串
     */
    public static String sqlInject(String str, String escape) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        escape = Optional.ofNullable(escape).orElse("");

        String regex = new StringBuilder("[\\w-.").append(escape).append("]").toString();
        String regexReverse = new StringBuilder("[^\\w-.").append(escape).append("]").toString();
        /**去掉特殊字符（只留下字母数字下划线中线线点号和逗号）**/
        str = str.replaceAll(regexReverse, "");
        /**
         * str必须同时满足以字母开头，以字母数字下划线结尾
         */
        String regexStart = new StringBuilder("^[A-Za-z_]+").append(regex).append("*").toString();
        String regexEnd = new StringBuilder(regex).append("*[\\w]+$").toString();
        /**
         * 如果不同时满足，包括非法字符
         */
        if (!(Pattern.matches(regexStart, str) && Pattern.matches(regexEnd, str))) {
            throw new AppException("包含非法字符");
        }
        return str;
    }
}
