package com.goddess.common.rocketmq;

import com.alibaba.fastjson.JSONObject;
import com.goddess.common.config.RocketMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/16 下午10:16
 * @Copyright © 女神帮
 */
@Component
@Slf4j
@ConditionalOnProperty(prefix = "rocketmq", name = "enable", havingValue = "true")
public class RocketMqProducer {
    RocketMqConfig rocketMqConfig = new RocketMqConfig();
    @Autowired
    private Producer producer;


    /**
     * 事务消息
     * @param listener
     * @param topic
     * @param tag
     * @param msg
     * @param arg
     * @return
     */
    public TransactionSendResult sendMsgTransaction(MyTransactionListener listener,String topic, String tag, String msg,Object arg) {
        try {
            TransactionSendResult transactionSendResult = TransactionListenerFactory.getTransactionProducer(listener).send(topic,tag,msg,arg);
            if (transactionSendResult != null) {
                log.info(new Date() + " 事务发送消息成功  Topic:" + topic + "tag:" + tag + " msgId is: " + transactionSendResult.getMsgId() + " msg:" + msg+"\n"+
                        JSONObject.toJSONString(transactionSendResult));
            } else {
                log.warn("事务发送消息失败 sendResult is null.........");
            }
            return transactionSendResult;
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return null;
    }
    public TransactionSendResult sendMsgTransaction(MyTransactionListener listener,String topic, String tag, String msg) {
        return sendMsgTransaction(listener,topic,tag,msg,null);
    }

    /**
     * 同步发送实体对象消息
     * 可靠同步发送：同步发送是指消息发送方发出数据后，会在收到接收方发回响应之后才发下一个数据包的通讯方式；
     * 特点：速度快；有结果反馈；数据可靠；
     * 应用场景：应用场景非常广泛，例如重要通知邮件、报名短信通知、营销短信系统等；
     */
    public String sendMsg(String topic, String tag, String msg) {
        String msgId="0";
        try {
            Message message = new Message(topic, tag, msg.getBytes(Charset.forName("utf-8")));
            SendResult sendResult = producer.getProducer().send(message);
            if (sendResult != null) {
                msgId=sendResult.getMsgId();
                log.info(new Date() + " 发送消息成功  Topic:" + message.getTopic() + "tag:" + tag + " msgId is: " + msgId + " msg:" + msg);
            } else {
                log.warn("发送消息失败 sendResult is null.........");
            }
            return msgId;
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msgId;
    }
    /**
     * 异步发送消息
     * 可靠异步发送：发送方发出数据后，不等接收方发回响应，接着发送下个数据包的通讯方式；
     * 特点：速度快；有结果反馈；数据可靠；
     * 应用场景：异步发送一般用于链路耗时较长,对 rt响应时间较为敏感的业务场景,例如用户视频上传后通知启动转码服务,转码完成后通知推送转码结果等；
     *
     * @param msg
     * @return
     */
    public void sendMsgAsy(String topic, String tag, String msg, Callable success,Callable failed) {
        Message message = new Message(topic, tag, msg.getBytes(Charset.forName("utf-8")));
        try {
            producer.getProducer().send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    ///消息发送成功
                    log.info(new Date() + " 发送异步消息成功  Topic:" + message.getTopic() + "tag:" + tag + " msgId is: " + sendResult.getMsgId() + " msg:" + msg);
                    try {
                        if(null != success){
                            success.call();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onException(Throwable throwable) {
                    //消息发送失败
                    log.warn("发送异步消息失败 . execption=" + throwable.getMessage());
                    throwable.printStackTrace();
                    try {
                        if(null != failed){
                            failed.call();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if(null != failed){
                failed.call();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendMsgAsy(String topic, String tag, String msg,Callable failed) {
        this.sendMsgAsy(topic,tag,msg,null,failed);
    }
    public void sendMsgAsy(String topic, String tag, String msg) {
        this.sendMsgAsy(topic,tag,msg,null,null);
    }

    /**
     * 单向发送
     * 单向发送：只负责发送消息，不等待服务器回应且没有回调函数触发，即只发送请求不等待应答；此方式发送消息的过程耗时非常短，一般在微秒级别；
     * 特点：速度最快，耗时非常短，毫秒级别；无结果反馈；数据不可靠，可能会丢失；
     * 应用场景：适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集；
     *
     * @return
     */
    public boolean sendMsgOneway(String topic, String tag, String msg) {
        Message message = new Message(topic, tag, JSONObject.toJSONString(msg).getBytes(Charset.forName("utf-8")));
        try {
            producer.getProducer().sendOneway(message);
            return true;
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
