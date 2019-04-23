package com.qflu.verify.controller;

import com.qflu.verify.annotation.LocalLock;
import com.qflu.verify.service.VerifyDBService;
import com.qflu.verify.utils.VerifyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.text.ParseException;
import java.util.Date;

@Validated // 参数校验
@Controller
@RequestMapping("/verify")
public class VerifyController {

    private static final Logger log = LoggerFactory.getLogger(VerifyController.class);

    private VerifyDBService verifyDBService;

    @Autowired
    public VerifyController(VerifyDBService verifyDBService) {
        this.verifyDBService = verifyDBService;
    }

    @GetMapping("/index")
    public String index(HttpServletRequest request){
        // TODO 与上面的写法不同，但是结果一致。
        // 设置属性
        // request.setAttribute("title", "thymeleaf WEB页面");
        // request.setAttribute("desc", "欢迎进入依然的demo~");
        // 返回的 index 默认映射到 src/main/resources/templates/xx.html
        return "index";
    }

    @GetMapping("/byCrtDt")
    public String byCrtDt(HttpServletRequest request){
        return "byCrtDt";
    }

    @GetMapping("/byId")
    public String byId(HttpServletRequest request){
        return "byId";
    }

    // 意味着会将 arg[0] 替换成第一个参数的值，生成后的新 key 将被缓存起来；
    @LocalLock(key = "verify:arg[0]")
    @ResponseBody
    @PostMapping("/crtDt")
    public String verifyByCrtDt(@NotEmpty String tableName, String startDate, @Min(1) Integer interval) {
        startDate = startDate + " 00:00:00";
        try {
            Date start = VerifyUtil.sdf.parse(startDate);
            if (start.compareTo(new Date()) > 0) {
                return "startDate 必须位于当前时间之前";
            }
        } catch (ParseException e) {
            return "startDate 格式错误，要求为 [yyyy-MM-dd]";
        }
        log.info("[表名] - [{}] ， [起始时间] - [{}] ， [间隔时间] - [{}]", tableName, startDate, interval);
        verifyDBService.queryByCrtDt(tableName, startDate, interval);
        return "success";
    }

    // 意味着会将 arg[0] 替换成第一个参数的值，生成后的新 key 将被缓存起来；
    @LocalLock(key = "verify:arg[0]")
    @ResponseBody
    @PostMapping("/id")
    public String verifyById(@NotEmpty String tableName, @Min(1000) @Max(10000) Integer pageSize) {
        log.info("[表名] - [{}] ， [每次查询数量] - [{}]", tableName, pageSize);
        verifyDBService.queryById(tableName, pageSize);
        return "success";
    }
}
