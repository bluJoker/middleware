package com.debug.middleware.server.service.impl;

import com.debug.middleware.server.dto.RedPacketDto;
import com.debug.middleware.server.dto.RedPacketMQDto;
import com.debug.middleware.server.rabbitmq.publisher.RedPacketPublisher;
import com.debug.middleware.server.service.IRedPacketService;
import com.debug.middleware.server.service.IRedService;
import com.debug.middleware.server.utils.RedPacketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedPacketService implements IRedPacketService {

    private static final Logger log = LoggerFactory.getLogger(RedPacketService.class);

//    private final SnowFlake snowFlake=new SnowFlake(2,3);

    private static final String keyPrefix = "redis:red:packet:";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IRedService redService;

    @Autowired
    private RedPacketPublisher redPacketPublisher;


    /**
     * 发红包
     *
     * @throws Exception
     */
    @Override
    public String handOut(RedPacketDto dto) throws Exception {
        if (dto.getTotal() > 0 && dto.getAmount() > 0) {
            //生成随机金额
            List<Integer> list = RedPacketUtil.divideRedPackage(dto.getAmount(), dto.getTotal());

            //生成红包全局唯一标识,并将随机金额、个数入缓存
            String timestamp = String.valueOf(System.nanoTime());
            String redId = new StringBuffer(keyPrefix).append(dto.getUserId()).append(":").append(timestamp).toString();
            redisTemplate.opsForList().leftPushAll(redId, list);

            String redTotalKey = redId + ":total";
            redisTemplate.opsForValue().set(redTotalKey, dto.getTotal());

            //异步记录红包发出的记录-包括个数与随机金额
            redService.recordRedPacket(dto, redId, list);

            return redId;
        } else {
            throw new Exception("系统异常-分发红包-参数不合法!");
        }
    }

    /**
     * 加分布式锁的情况
     * 抢红包-分“点”与“抢”处理逻辑
     *
     * @throws Exception
     *
     *
     *     用户1      用户2      用户3      用户1'
     *       |         |         |         |
     *       |         |         |         |
     *      \/        \/        \/        \/
     *     锁1        锁2        锁3    尝试获取锁1，setIfAbsent返回false，抢红包处理逻辑不走，即抢红包失败。
     *                                 就算用户1’和用户1同时去setIfAbsent，但由于该方法具备原子性(单线程)操作的特性
     *                                 (其实Redis的单个方法都是原子性的)，该条命令要么执行完要么不执行。因而当多个并发
     *                                 的线程同一时刻调用setIfAbsent()时，Redis的底层是会将线程加入"队列"排队处理的。
     *
     */
    @Override
    public BigDecimal rob(Integer userId, String redId) throws Exception {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        //用户是否抢过该红包
        Object obj = valueOperations.get(redId + userId + ":rob");
        if (obj != null) {
            return new BigDecimal(obj.toString());
        }

        //"点红包"
        Boolean res = click(redId);
        if (res) {
            //上锁:一个红包每个人只能抢一次随机金额；一个人每次只能抢到红包的一次随机金额  即要永远保证 1对1 的关系
            final String lockKey = redId + userId + "-lock";
            Boolean lock = valueOperations.setIfAbsent(lockKey, redId);
            redisTemplate.expire(lockKey, 24L, TimeUnit.HOURS);
            try {
                if (lock) {

                    //"抢红包"-且红包有钱
                    Object value = redisTemplate.opsForList().rightPop(redId);
                    if (value != null) {
                        //红包个数减一
                        String redTotalKey = redId + ":total";

                        Integer currTotal = valueOperations.get(redTotalKey) != null ? (Integer) valueOperations.get(redTotalKey) : 0;
                        valueOperations.set(redTotalKey, currTotal - 1);


                        //将红包金额返回给用户的同时，将抢红包记录入数据库与缓存
                        BigDecimal result = new BigDecimal(value.toString()).divide(new BigDecimal(100));

                        RedPacketMQDto redPacketMQDto = new RedPacketMQDto();
                        redPacketMQDto.setUserId(userId);
                        redPacketMQDto.setRedId(redId);
                        redPacketMQDto.setAmount(new BigDecimal(value.toString()));

                        // 使用rabbitmq异步记录抢红包明细到数据库中
//                        redService.recordRobRedPacket(userId, redId, new BigDecimal(value.toString()));
                        redPacketPublisher.sendMsg(redPacketMQDto);

                        valueOperations.set(redId + userId + ":rob", result, 24L, TimeUnit.HOURS);

                        log.info("当前用户抢到红包了：userId={} key={} 金额={} ", userId, redId, result);
                        return result;
                    }

                }
            } catch (Exception e) {
                throw new Exception("系统异常-抢红包-加分布式锁失败!");
            }
        }
        return null;
    }


//    /**
//     * 不加分布式锁的情况
//     * 抢红包-分“点”与“抢”处理逻辑
//     *
//     * @param userId 当前用户id - 抢红包者
//     * @param redId 红包全局唯一标识串
//     * @return 返回抢到的红包金额或者抢不到红包金额的null
//     * @throws Exception
//     *
//     * 方法前加上synchronized加锁是可以防止高并发下同一用户抢到多个红包的情况发生的：
//     *          单用户多个请求同时到达，判断缓存中发现没有，都执行了后面的抢红包逻辑，并抢到
//     * PS：对集群无效，因为synchronized跟单一服务节点所在的JVM相关联
//     */
//    @Override
//    public BigDecimal rob(Integer userId, String redId) throws Exception {
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//
//        // 在处理用户抢红包之前，需要先判断一下当前用户是否已经抢过该红包了
//        Object obj = valueOperations.get(redId + userId + ":rob");
//        if (obj != null) {
//            log.info("当前用户已经抢过该红包了: {}", userId);
//            // 如果已经抢过了，则直接返回红包金额到前端显示
//            return new BigDecimal(obj.toString());
//        }
//
//        //"点红包" 业务逻辑 - 主要用于判断缓存系统中是否仍然有红包，即红包剩余个数是否大于0
//        Boolean res = click(redId);
//        if (res) {
//            //"抢红包"-且红包有钱
//            Object value = redisTemplate.opsForList().rightPop(redId);
//            if (value != null) {
//                //红包个数减一
//                String redTotalKey = redId + ":total";
//
//                Integer currTotal =
//                        valueOperations.get(redTotalKey) != null ? (Integer) valueOperations.get(redTotalKey) : 0;
//                valueOperations.set(redTotalKey, currTotal - 1);
//
//
//                //将红包金额返回给用户的同时，将抢红包记录入数据库与缓存
//                BigDecimal result = new BigDecimal(value.toString()).divide(new BigDecimal(100));
//                redService.recordRobRedPacket(userId, redId, new BigDecimal(value.toString()));
//
//                valueOperations.set(redId + userId + ":rob", result, 24L, TimeUnit.HOURS);
//
//                log.info("当前用户抢到红包了：userId={} key={} 金额={} ", userId, redId, result);
//                return result;
//            }
//
//        }
//        return null;
//    }


    /**
     * 点红包-返回true，则代表红包还有，个数>0
     *
     * @throws Exception
     */
    private Boolean click(String redId) throws Exception {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        String redTotalKey = redId + ":total";
        Object total = valueOperations.get(redTotalKey);

        // Integer.valueOf(total.toString())：先转换为字符串，再转换为整型
        if (total != null && Integer.valueOf(total.toString()) > 0) {
            return true;
        }
        return false;
    }
}
