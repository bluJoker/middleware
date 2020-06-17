package com.debug.middleware.server.service.impl;

import com.debug.middleware.model.dao.RedDetailMapper;
import com.debug.middleware.model.dao.RedRecordMapper;
import com.debug.middleware.model.dao.RedRobRecordMapper;
import com.debug.middleware.model.entity.RedDetail;
import com.debug.middleware.model.entity.RedRecord;
import com.debug.middleware.model.entity.RedRobRecord;
import com.debug.middleware.server.dto.RedPacketDto;
import com.debug.middleware.server.dto.RedPacketMQDto;
import com.debug.middleware.server.service.IRedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@EnableAsync
public class RedService implements IRedService {

    private static final Logger log = LoggerFactory.getLogger(RedService.class);

    @Autowired
    private RedRecordMapper redRecordMapper;

    @Autowired
    private RedDetailMapper redDetailMapper;

    @Autowired
    private RedRobRecordMapper redRobRecordMapper;

    /**
     * 发红包记录
     *
     * @param dto
     * @param redId
     * @param list
     * @throws Exception
     */
    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) throws Exception {
        RedRecord redRecord = new RedRecord();
        redRecord.setUserId(dto.getUserId());
        redRecord.setRedPacket(redId);
        redRecord.setTotal(dto.getTotal());
        redRecord.setAmount(BigDecimal.valueOf(dto.getAmount()));
        redRecord.setCreateTime(new Date());

        redRecordMapper.insertSelective(redRecord);

        log.info("record: {}", redRecord.getId());

        RedDetail detail;
        for (Integer i : list) {
            detail = new RedDetail();

            // RedRecordMapper.xml中
            // <insert id="insertSelective" parameterType="com.debug.middleware.model.entity.RedRecord"
            //          keyProperty="id" useGeneratedKeys="true">
            // 获取主键值
            detail.setRecordId(redRecord.getId());
            detail.setAmount(BigDecimal.valueOf(i));
            detail.setCreateTime(new Date());

            redDetailMapper.insertSelective(detail);
        }
    }

    /**
     * 抢红包记录
     *
     * @param userId
     * @param redId
     * @param amount
     * @throws Exception
     */
    @Override
    @Async
    public void recordRobRedPacket(RedPacketMQDto dto) throws Exception {
        RedRobRecord redRobRecord=new RedRobRecord();
        redRobRecord.setUserId(dto.getUserId());
        redRobRecord.setRedPacket(dto.getRedId());
        redRobRecord.setAmount(dto.getAmount());
        redRobRecord.setRobTime(new Date());

        redRobRecordMapper.insertSelective(redRobRecord);
    }


    /**
     * 抢红包记录
     * @param userId
     * @param redId
     * @param amount
     * @throws Exception
     */
//    @Override
//    @Async
//    public void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) throws Exception {
//        RedRobRecord redRobRecord=new RedRobRecord();
//        redRobRecord.setUserId(userId);
//        redRobRecord.setRedPacket(redId);
//        redRobRecord.setAmount(amount);
//        redRobRecord.setRobTime(new Date());
//        redRobRecordMapper.insertSelective(redRobRecord);
//    }

}
