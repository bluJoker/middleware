package com.debug.middleware.server.rabbitmq.consumer;

import com.debug.middleware.server.rabbitmq.entity.DeadInfo;
import com.debug.middleware.server.rabbitmq.publisher.DeadPublisher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DeadConsumerTest {

    private static final Logger log = LoggerFactory.getLogger(DeadConsumerTest.class);

    @Autowired
    private DeadPublisher deadPublisher;

    @Test
    public void consumeMsg() throws InterruptedException {

        deadPublisher.sendMsg(new DeadInfo(2, "死信消息1"));
        Thread.sleep(5000);

        deadPublisher.sendMsg(new DeadInfo(4, "死信消息2"));

        Thread.sleep(60000);
    }
}