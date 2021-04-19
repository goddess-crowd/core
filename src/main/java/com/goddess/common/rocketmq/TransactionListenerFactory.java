package com.goddess.common.rocketmq;

import com.goddess.common.config.RocketMqConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/17 上午1:47
 * @Copyright © 女神帮
 */
@Component
@ConditionalOnProperty(prefix = "rocketmq", name = "enable", havingValue = "true")
public class TransactionListenerFactory {
    private RocketMqConfig rocketMqConfig = new RocketMqConfig();
    static Map<MyTransactionListener, TransactionProducer> transactionProducerMap = new HashMap<>();

    @Autowired
    public void register(MyTransactionListener[] instances) {
        if (instances != null && instances.length > 0) {
            for (MyTransactionListener oth : instances) {
                transactionProducerMap.put(oth, new TransactionProducer(rocketMqConfig,oth));
            }
        }
    }

    public static TransactionProducer getTransactionProducer(MyTransactionListener listener) {
        if (transactionProducerMap.containsKey(listener)) {
            return transactionProducerMap.get(listener);
        } else {
            return null;
        }
    }
}
