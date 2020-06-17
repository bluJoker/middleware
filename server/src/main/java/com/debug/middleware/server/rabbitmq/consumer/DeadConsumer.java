package com.debug.middleware.server.rabbitmq.consumer;

import com.debug.middleware.server.rabbitmq.entity.DeadInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class DeadConsumer {
    private static final Logger log = LoggerFactory.getLogger(DeadConsumer.class);

    @RabbitListener(queues = "${mq.consumer.real.queue.name}", containerFactory = "singleListenerContainer")
    public void consumeMsg(@Payload DeadInfo deadInfo) {
        try {
            log.info("死信队列实战-监听真正队列-消费队列中的消息,监听到消息内容为：{}", deadInfo);
        } catch (Exception e) {
            log.error("死信队列实战-监听真正队列-消费队列中的消息 - 面向消费者-发生异常：{} ", deadInfo, e.fillInStackTrace());
        }
    }
}
