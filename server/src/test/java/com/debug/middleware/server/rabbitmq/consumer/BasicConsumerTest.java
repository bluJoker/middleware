package com.debug.middleware.server.rabbitmq.consumer;

import com.debug.middleware.server.rabbitmq.entity.Person;
import com.debug.middleware.server.rabbitmq.publisher.BasicPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BasicConsumerTest {

    private static final Logger log= LoggerFactory.getLogger(BasicConsumerTest.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BasicPublisher basicPublisher;


    @Test
    public void consumeMsg() {

        String msg = "~~~~这是一串字符串消息~~~~";
//        basicPublisher.sendMsg(msg);

    }

    @Test
    public void consumeObjectMsg() {

        Person p = new Person(1, "大圣", "debug");
        basicPublisher.sendObjectMsg(p);

    }
}