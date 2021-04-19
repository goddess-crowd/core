package com.goddess.common.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;


/**
 * 用来存储header信息，当调用其它中心时放到header中传递过去
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/7 上午9:35
 * @Copyright © 女神帮
 */
public class ExtraParamUtil {

    private final static TransmittableThreadLocal<ExtraParam> extraParamLocal = new TransmittableThreadLocal<>();

    public static ExtraParam getExtraParam() {
        if(!Optional.ofNullable(extraParamLocal.get()).isPresent()){
            extraParamLocal.set(new ExtraParam());
        }
        return extraParamLocal.get();
    }

    public static void setExtraParam(ExtraParam extraParam) {
        extraParamLocal.set(extraParam);
    }

    public final static Map<String, String> map = Maps.newHashMap();

    public static void destroy() {
        extraParamLocal.remove();
    }

}
