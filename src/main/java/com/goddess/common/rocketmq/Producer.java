package com.goddess.common.rocketmq;


import com.goddess.common.config.RocketMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/16 下午10:10
 * @Copyright © 女神帮
 */
@Component
@Slf4j
@ConditionalOnProperty(prefix = "rocketmq", name = "enable", havingValue = "true")
public class Producer {

    private RocketMqConfig rocketMqConfig = new RocketMqConfig();

    private DefaultMQProducer producer;

    public Producer() {
        producer = new DefaultMQProducer(rocketMqConfig.getProducerGroup());
        //指定nameserver地址，多个地址使用,分隔
        producer.setNamesrvAddr(rocketMqConfig.getNamesrvAddr());
    }

    public DefaultMQProducer getProducer() {
        return this.producer;
    }
    @PostConstruct
    public void init() {
        log.info("{}组mq生产者启动=",rocketMqConfig.getProducerGroup());
        start();
    }
    public void start() {
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        this.producer.shutdown();
    }
}
