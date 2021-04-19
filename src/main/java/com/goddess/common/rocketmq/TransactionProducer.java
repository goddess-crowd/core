package com.goddess.common.rocketmq;

import com.alibaba.fastjson.JSONObject;
import com.goddess.common.config.RocketMqConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/17 上午12:38
 * @Copyright © 女神帮
 */
public class TransactionProducer{

    private TransactionMQProducer producer;

    public TransactionProducer(RocketMqConfig rocketMqConfig, MyTransactionListener transactionListener) {
        producer = new TransactionMQProducer("userProducer_t1");
        producer.setNamesrvAddr(rocketMqConfig.getNamesrvAddr());
        producer.setSendMsgTimeout(Integer.MAX_VALUE);
        //执行任务的线程池
        producer.setExecutorService(new ThreadPoolExecutor(5, 10, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(50)));
        //用于执行本地事务和事务状态回查的监听器
        producer.setTransactionListener(transactionListener);
        this.start();
    }

    private void start(){
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
    //事务消息发送
    public TransactionSendResult send( String topic,String tag, String data,Object arg) throws MQClientException {
        Message message = new Message(topic,tag,data.getBytes());
        return this.producer.sendMessageInTransaction(message, null);
    }
}
