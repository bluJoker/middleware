package com.debug.middleware.server.config;

import com.debug.middleware.server.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CommonConfigTest {

    private static final Logger log = LoggerFactory.getLogger(CommonConfigTest.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test1() {
        log.info("----开始RedisTemplate操作组件实战----");

        final String key = "redis:template:one:string";

        final String content = "RedisTemplate实战字符串信息";

        ValueOperations valueOperations = redisTemplate.opsForValue();

        log.info("写入缓存中的内容：{} ", content);

        valueOperations.set(key, content);
        Object result = valueOperations.get(key);
        log.info("读取出来的内容：{} ", result);
    }

    @Test
    public void testSeri() throws IOException {
        log.info("----开始RedisTemplate操作组件实战2----");

        User user = new User(1, "debug", "阿修罗");

        final String key = "redis:template:two:object";

        // 借助ObjectMapper组件提供的Jackson序列化框架(JSON序列化框架的一种)对对象进行序列化与反序列化
        final String content = objectMapper.writeValueAsString(user);

        ValueOperations valueOperations = redisTemplate.opsForValue();

        valueOperations.set(key, content);

        log.info("写入缓存对象的信息：{} ", user);

        Object result = valueOperations.get(key);

        if (result != null){
            User resultUser = objectMapper.readValue(result.toString(),
                    User.class);
            log.info("读取缓存内容并反序列化后的结果：{} ", resultUser);
        }
    }
}