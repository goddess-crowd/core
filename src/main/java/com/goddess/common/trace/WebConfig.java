package com.goddess.common.trace;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/9 下午11:12
 * @Copyright © 女神帮
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private TraceInterceptor traceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceInterceptor)
                .addPathPatterns("/**");
    }
}
