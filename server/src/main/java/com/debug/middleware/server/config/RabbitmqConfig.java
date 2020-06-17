package com.debug.middleware.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

// Rabbitmq 自定义注入配置Bean相关组件
@Configuration
public class RabbitmqConfig {
    private static final Logger log = LoggerFactory.getLogger(RabbitmqConfig.class);

    // rabbitmq链接工厂实例
    @Autowired
    private CachingConnectionFactory connectionFactory;

    // 消息监听器所在的容器工厂配置类实例
    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    // 单一消费者实例配置
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer(){
        // 定义消息监听器所在的容器工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        // 设置容器工厂所用的实例
        factory.setConnectionFactory(connectionFactory);

        // 设置消息在传输中的格式，在这采用JSON格式进行传输
        factory.setMessageConverter(new Jackson2JsonMessageConverter());

        // 设置并发消费者实例的初始数量。在这里为1
        factory.setConcurrentConsumers(1);

        // 设置并发消费者实例的最大数量。在这里为1
        factory.setMaxConcurrentConsumers(1);

        // 设置并发消费者实例中每个实例拉取的消息数量。在这里为1
        factory.setPrefetchCount(1);

//        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);

        return factory;
    }

    // 多个消费者实例的配置，主要针对高并发业务场景
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        // 设置容器工厂所用的实例
        factoryConfigurer.configure(factory, connectionFactory);

        factory.setMessageConverter(new Jackson2JsonMessageConverter());

        // 设置消息的确认消费模式。在这里为NONE，表示不需要确认消费
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);

        factory.setConcurrentConsumers(10);

        factory.setMaxConcurrentConsumers(15);

        factory.setPrefetchCount(10);

        return factory;
    }

    /**
     * 自定义配置RabbitMQ发送消息的操作组件RabbitTemplate
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(){
        // 设置 “发送消息后进行确认”
        connectionFactory.setPublisherConfirms(true);

        // 设置 “发送消息后返回确认信息”
        connectionFactory.setPublisherReturns(true);

        // 构造发送消息组件实例对象
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // ???
        rabbitTemplate.setMandatory(true);

        // 发送消息后，如果发送成功，则输出 “消息发送成功” 的反馈信息
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                log.info("消息发送成功:correlationData({}),ack({}),cause({})",correlationData,b,s);
            }
        });

        // 发送消息后，如果发送失败，则输出 “消息发送失败-消息丢失” 的反馈信息
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",s1,s2,i,s,message);
            }
        });
        return rabbitTemplate;
    }

    //定义读取配置文件的环境变量实例
    @Autowired
    private Environment env;

    @Bean(name = "basicQueue")
    public Queue basicQueue(){
        return new Queue(env.getProperty("mq.basic.info.queue.name"), true);
    }

    @Bean
    public DirectExchange basicExchange(){
        return new DirectExchange(env.getProperty("mq.basic.info.exchange.name"), true, false);
    }

    @Bean
    public Binding basicBinding(){
        return BindingBuilder.bind(basicQueue()).to(basicExchange()).with(env.getProperty("mq.basic.info.routing.key.name"));
    }

    @Bean
    public Queue objectQueue(){
        return new Queue(env.getProperty("mq.object.info.queue.name"), true);
    }

    @Bean
    public DirectExchange objectExchange(){
        return new DirectExchange(env.getProperty("mq.object.info.exchange.name"), true, false);
    }

    @Bean
    public Binding objectBinding(){
        return BindingBuilder.bind(objectQueue()).to(objectExchange()).with(env.getProperty("mq.object.info.routing.key.name"));
    }

    // 创建死信队列
    @Bean
    public Queue basicDeadQueue(){
        // 创建死信队列的组成成分map，用于存放组成成分的相关成员
        Map<String, Object> args = new HashMap<>();
        // 成员1：死信交换机
        args.put("x-dead-letter-exchange", env.getProperty("mq.dead.exchange.name"));
        // 成员2：死信路由
        args.put("x-dead-letter-routing-key", env.getProperty("mq.dead.routing.key.name"));
        // 成员3：设定TTL，单位为ms，在这里指的是10s
        args.put("x-message-ttl", 50000);

        return new Queue(env.getProperty("mq.dead.queue.name"),
                true,
                false,
                false,
                args);
    }

    @Bean
    public TopicExchange basicProducerExchange(){
        return new TopicExchange(env.getProperty("mq.producer.basic.exchange.name"), true, false);
    }

    // 将死信队列绑定到基本交换机
    @Bean
    public Binding basicProducerBinding(){

        return BindingBuilder.bind(basicDeadQueue()).to(basicProducerExchange()).with(env.getProperty("mq.producer.basic.routing.key.name"));
    }

    //创建真正队列 - 面向消费者
    @Bean
    public Queue realConsumerQueue() {
        return new Queue(env.getProperty("mq.consumer.real.queue.name"), true);
    }

    //创建死信交换机
    @Bean
    public TopicExchange basicDeadExchange() {
        return new TopicExchange(env.getProperty("mq.dead.exchange.name"), true, false);
    }

    //创建死信路由及其绑定
    @Bean
    public Binding basicDeadBinding() {
        return BindingBuilder.bind(realConsumerQueue()).to(basicDeadExchange()).with(env.getProperty("mq.dead.routing.key.name"));
    }


    // -----------------------------------------------------------------------------------------------------------------
    // 商城平台订单支付超时
    // -----------------------------------------------------------------------------------------------------------------

    // 创建死信队列
    @Bean
    public Queue orderDeadQueue(){
        // 创建死信队列的组成成分map，用于存放组成成分的相关成员
        Map<String, Object> args = new HashMap<>();
        // 成员1：死信交换机
        args.put("x-dead-letter-exchange", env.getProperty("mq.order.dead.exchange.name"));
        // 成员2：死信路由
        args.put("x-dead-letter-routing-key", env.getProperty("mq.order.dead.routing.key.name"));
        // 成员3：设定TTL，单位为ms，在这里指的是10s
        args.put("x-message-ttl", 20000);

        return new Queue(env.getProperty("mq.order.dead.queue.name"),
                true,
                false,
                false,
                args);
    }

    @Bean
    public TopicExchange orderProducerExchange(){
        return new TopicExchange(env.getProperty("mq.producer.order.exchange.name"), true, false);
    }

    // 将死信队列绑定到基本交换机
    @Bean
    public Binding orderProducerBinding(){

        return BindingBuilder.bind(orderDeadQueue()).to(orderProducerExchange()).with(env.getProperty("mq.producer.order.routing.key.name"));
    }

    //创建真正队列 - 面向消费者
    @Bean
    public Queue realOrderConsumerQueue() {
        return new Queue(env.getProperty("mq.consumer.order.real.queue.name"), true);
    }

    //创建死信交换机
    @Bean
    public TopicExchange basicOrderDeadExchange() {
        return new TopicExchange(env.getProperty("mq.order.dead.exchange.name"), true, false);
    }

    //创建死信路由及其绑定
    @Bean
    public Binding basicOrderDeadBinding() {
        return BindingBuilder.bind(realOrderConsumerQueue()).to(basicOrderDeadExchange()).with(env.getProperty("mq.order.dead.routing.key.name"));
    }

}