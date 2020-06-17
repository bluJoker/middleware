package com.debug.middleware.server.controller.rabbitmq;

import com.debug.middleware.api.enums.StatusCode;
import com.debug.middleware.api.response.BaseResponse;
import com.debug.middleware.server.dto.UserOrderDto;
import com.debug.middleware.server.service.DeadUserOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserOrderController {

    private static final Logger log = LoggerFactory.getLogger(UserOrderController.class);

    //定义请求前缀
    private static final String prefix = "user/order";
    //用户下单处理服务实例
    @Autowired
    private DeadUserOrderService deadUserOrderService;

    /**
     * 用户下单
     *
     * @return
     */
    @RequestMapping(value = prefix + "/push",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse login(@RequestBody @Validated UserOrderDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            // 调用Service层真正处理用户下单的业务逻辑
            deadUserOrderService.pushUserOrder(dto);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }
}
