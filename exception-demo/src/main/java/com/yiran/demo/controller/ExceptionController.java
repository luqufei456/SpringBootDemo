package com.yiran.demo.controller;

import com.yiran.demo.exception.YiRanException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* 全局异常演示
* */
@RestController
public class ExceptionController {
    @GetMapping("/test3")
    public String test3(Integer num) {
        // TODO 演示需要，实际上参数是否为空通过 @RequestParam(required = true)  就可以控制
        if (num == null) {
            throw new YiRanException(400, "num 不可为空");
        }
        int i = 10 / num;
        return "result:" + i;
    }
}
