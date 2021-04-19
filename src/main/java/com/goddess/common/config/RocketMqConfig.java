package com.goddess.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/16 下午10:17
 * @Copyright © 女神帮
 */
//@ConfigurationProperties(prefix = "rocketmq")
@Data
public class RocketMqConfig {


    public String namesrvAddr = "127.0.0.1:9876";

    public String producerGroup = "userProducer";


}
