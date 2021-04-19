package com.goddess.common.util;

/**
 * Redis所有Keys
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/7 上午9:35
 * @Copyright © 女神帮
 */
public class RedisKeys {

    public static String getSysConfigKey(String key) {
        return "sys:config:" + key;
    }

    public static String getPermsKey(String key) {
        return "sys:perms:" + key;
    }

    public static String getMqKey(String group, String key) {
        return "sys:mq:" + group + ":" + key;
    }
}
