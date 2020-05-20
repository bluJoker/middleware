package com.debug.middleware.server.controller;

import com.debug.middleware.server.service.CachePassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CachePassController {

    private static final Logger log = LoggerFactory.getLogger(CachePassService.class);

    @Autowired
    private CachePassService cachePassService;

    private static final String keyPrefix = "item:";

    private static final String prefix = "cache/pass";

    // 获取热销商品信息
    @GetMapping(prefix + "/item/info/{itemCode}")
    public Map<String, Object> getItem(@PathVariable("itemCode") String itemCode){

        Map<String, Object> resmap = new HashMap<>();
        resmap.put("code", 0);
        resmap.put("msg", "成功");

        //此处不需要序列化了，因为在service层已经做了序列化
        try {
            // 调用缓存穿透处理服务类得到返回结果，并将其添加进结果Map中
            resmap.put("data", cachePassService.getItemByCode(itemCode));
        } catch (IOException e) {
            resmap.put("code", -1);
            resmap.put("msg", "失败" + e.getMessage());
        }
        return resmap;
    }



}
