package com.goddess.common.util;

import org.apache.commons.lang3.BooleanUtils;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/9 下午12:43
 * @Copyright © 女神帮
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {
    /***
     * 转换为 Long 对象
     * @param obj
     * @return
     */
    public static Long toLong(final Object obj) {
        return toLong(obj, null);
    }

    /***
     * 转换为 Long 对象
     * @param obj
     * @param defaultValue
     * @return
     */
    public static Long toLong(final Object obj, final Long defaultValue) {
        Long value = null;
        if (obj == null) {
            value = defaultValue;
        } else if (obj instanceof Long) {
            value = (Long)obj;
        } else if (obj instanceof Number) {
            Number num = (Number)obj;
            value = num.longValue();
        } else if (obj instanceof Boolean) {
            Boolean num = (Boolean)obj;
            Integer inum= BooleanUtils.toIntegerObject(num);
            value = inum.longValue();
        } else {
            try {
                value = Long.parseLong(obj.toString());
            } catch (final NumberFormatException nfe) {
                value = defaultValue;
            }
        }
        return value;
    }
}
