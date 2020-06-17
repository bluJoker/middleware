package com.debug.middleware.server.rabbitmq.publisher;

import com.debug.middleware.server.dto.RedPacketDto;
import com.debug.middleware.server.dto.RedPacketMQDto;
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
public class RedPacketPublisher {
    private static final Logger log = LoggerFactory.getLogger(RedPacketPublisher.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private ObjectMapper objectMapper;


    public void sendMsg(RedPacketMQDto dto){
        try {
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.setExchange(env.getProperty("mq.basic.info.exchange.name"));
            rabbitTemplate.setRoutingKey(env.getProperty("mq.basic.info.routing.key.name"));

            rabbitTemplate.convertAndSend(dto, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    MessageProperties messageProperties = message.getMessageProperties();
                    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

                    messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,
                            RedPacketMQDto.class);

                    return message;
                }
            });

            log.info("系统日志记录-生产者-将登录成功后的用户相关信息发送给队列-内容：{}", dto);

        }catch (Exception e){
            log.error("系统日志记录-生产者-将登录成功后的用户相关信息发送给队列-发生异常：{}", dto, e.fillInStackTrace());
        }
    }
}
