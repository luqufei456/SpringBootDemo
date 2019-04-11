package com.yiran.demo.controller;

import com.yiran.demo.annotation.DateTime;
import com.yiran.demo.groups.Groups;
import com.yiran.demo.pojo.Anime;
import com.yiran.demo.pojo.Book;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/*
* 参数校验
* */
@Validated
@RestController
public class ValidateController {

    @GetMapping("/test2")
    public String test2(@NotBlank(message = "name 不能为空") @Length(min = 2, max = 10, message = "name 长度必须在 {min} - {max} 之间") String name) {
        return "success";
    }

    @GetMapping("/test3")
    public String test3(@Validated Book book) {
        return "success";
    }

    @GetMapping("/test4")
    // 这里的 message 、 format 相当于把我们定义的重写了
    public String test4(@DateTime(message = "您输入的格式错误，正确的格式为：{format}", format = "yyyy-MM-dd HH:mm") String date){
        return "success";
    }

    @GetMapping("/insert")
    public String insert(@Validated(value = Groups.Default.class) Anime anime) {
        return "insert";
    }

    @GetMapping("/update")
    public String update(@Validated(value = {Groups.Default.class, Groups.Update.class}) Anime anime) {
        return "update";
    }
}
