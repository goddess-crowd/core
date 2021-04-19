package com.goddess.common.ribbon;


import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/11 上午2:09
 * @Copyright © 女神帮
 */
@Configuration
public class RibbonConfiguration {

    @Bean
    public IRule ribbonRule() {
        return new NacosSameClusterWeightedRule();
    }
}
