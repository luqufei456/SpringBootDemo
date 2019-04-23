package com.qflu.verify.controller;

import com.qflu.verify.service.VerifyDBService;
import com.qflu.verify.utils.VerifyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.text.ParseException;
import java.util.Date;

@Validated // 参数校验
@RestController
@RequestMapping("/verify")
public class VerifyController {

    private static final Logger log = LoggerFactory.getLogger(VerifyController.class);

    private VerifyDBService verifyDBService;

    @Autowired
    public VerifyController(VerifyDBService verifyDBService) {
        this.verifyDBService = verifyDBService;
    }

    @GetMapping
    public String verify(@NotEmpty String tableName, String startDate, @Min(1) Integer interval) {
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
}
