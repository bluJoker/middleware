package com.debug.middleware.server.service;

import com.debug.middleware.server.dto.RedPacketDto;
import com.debug.middleware.server.dto.RedPacketMQDto;

import java.math.BigDecimal;
import java.util.List;

public interface IRedService {
    void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) throws Exception;

    void recordRobRedPacket(RedPacketMQDto dto) throws Exception;

//    void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) throws Exception;

}
