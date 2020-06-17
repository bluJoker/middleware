package com.debug.middleware.server.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class RedPacketMQDto implements Serializable {
    private Integer userId;
    private String redId;
    private BigDecimal amount;
}
