package com.yiran.demo.controller;

import com.yiran.demo.annotation.LocalLock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    // 如果token=1 被替换成  key = "book:1"
    @LocalLock(key = "book:arg[0]")
    @GetMapping
    // @RequestParam,是获取前端传递给后端的参数，可以是get方式，也可以是post方式。
    public String query(@RequestParam String token) {
        return "success - " + token;
    }
}
