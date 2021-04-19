package com.goddess.common.trace;

import com.goddess.common.constant.GoddessEnum;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import feign.RequestInterceptor;
/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/9 下午11:15
 * @Copyright © 女神帮
 */
@Configuration
public class FeignInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {

        requestTemplate.header(GoddessEnum.TRACE_ID, MDC.get(GoddessEnum.TRACE_ID));

    }
}