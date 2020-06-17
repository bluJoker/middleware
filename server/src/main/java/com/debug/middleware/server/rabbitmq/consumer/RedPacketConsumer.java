package com.debug.middleware.server.rabbitmq.consumer;

import com.debug.middleware.server.dto.RedPacketDto;
import com.debug.middleware.server.dto.RedPacketMQDto;
import com.debug.middleware.server.service.impl.RedService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RedPacketConsumer {
    private static final Logger log = LoggerFactory.getLogger(RedPacketConsumer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedService redService;

    @RabbitListener(queues = "${mq.basic.info.queue.name}",
            containerFactory = "singleListenerContainer")
    public void consumeMsg(@Payload RedPacketMQDto dto,
                           @Headers Map<String, Object> headers,
                           Channel channel) throws IOException {

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
            log.info("系统日志记录-消费者-监听消费用户抢红包成功后的消息-内容：{}", dto);
            redService.recordRobRedPacket(dto);

            // 获取消息分发时的全局唯一标识


            // 执行完业务逻辑后，手动进行确认消费
            // 参数1：消息分发标识(全局唯一)
            // 参数2：是否允许批量确认消费
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("系统日志记录-消费者-监听消费用户抢红包成功后的消息-发生异常：{}", dto, e.fillInStackTrace());

            // 如果在处理消息的过程中发生了异常，则依旧需要人为手动确认消费掉该消息
            // 否则该消息将一直留在队列中，从而导致消息的重复消费
            channel.basicReject(deliveryTag, false);
        }
    }
}
