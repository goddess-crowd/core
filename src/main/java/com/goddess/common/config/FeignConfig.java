package com.goddess.common.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/16 下午3:25
 * @Copyright © 女神帮
 */
@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLevel() {
        return Logger.Level.FULL;
    }
}
