package com.debug.middleware.server.rabbitmq.publisher;

import com.debug.middleware.server.rabbitmq.entity.DeadInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DeadPublisher {
    private static final Logger log = LoggerFactory.getLogger(DeadPublisher.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;

    public void sendMsg(DeadInfo deadInfo) {
        try {
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.setExchange(env.getProperty("mq.producer.basic.exchange.name"));
            //设置基本路由
            rabbitTemplate.setRoutingKey(env.getProperty("mq.producer.basic.routing.key.name"));
            //发送对象类型的消息
            rabbitTemplate.convertAndSend(deadInfo, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //获取消息属性对象
                    MessageProperties messageProperties = message.getMessageProperties();
                    //设置消息的持久化策略
                    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

                    // 设置消息的TTL。当消息和队列同时都设置了TTL时，则取较短时间的值。单位为ms
                    messageProperties.setExpiration(String.valueOf(25000));

                    //设置消息头-即直接指定发送的消息所属的对象类型
                    messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, DeadInfo.class);

                    //设置消息的TTL - 当消息和队列同时都设置了TTL时，则取较短时间的值
                    //messageProperties.setExpiration(String.valueOf(10000));

                    return message;
                }
            });

            //打印日志
            log.info("死信队列实战-发送对象类型的消息入死信队列-内容为：{} ", deadInfo);
        } catch (Exception e) {
            log.error("死信队列实战-发送对象类型的消息入死信队列-发生异常：{} ", deadInfo, e.fillInStackTrace());
        }
    }
}
