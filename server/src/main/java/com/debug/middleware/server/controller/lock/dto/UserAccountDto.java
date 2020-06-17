package com.debug.middleware.server.controller.lock.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
public class UserAccountDto implements Serializable {
    private Integer userId; //用户账户Id
    private Double amount;  //提现金额
}
