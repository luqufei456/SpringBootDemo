package com.qflu.verify.service.impl;

import com.qflu.verify.dao.MasterDBDao;
import com.qflu.verify.dao.SlaveDBDao;
import com.qflu.verify.service.VerifyDBService;
import com.qflu.verify.utils.VerifyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class VerifyDBServiceImpl implements VerifyDBService {

    private MasterDBDao masterDBDao;
    private SlaveDBDao slaveDBDao;

    @Autowired
    public VerifyDBServiceImpl(MasterDBDao masterDBDao, SlaveDBDao slaveDBDao) {
        this.masterDBDao = masterDBDao;
        this.slaveDBDao = slaveDBDao;
    }

    @Override
    public void queryByCrtDt(String tableName, String startDate, Integer interval) {
        // slf4j，设置变量，在xml中使用这些定义日志的名称，可以选择将日志输出到哪。适用于多线程
        MDC.put("logName",tableName);
        Logger log = LoggerFactory.getLogger(tableName);

        // 如果表名过长，则直接停止查询，防止sql注入，这是因为表名是字符串拼接的，而不是占位符
        // 表名也不可以用占位符
        if (tableName.length() > 50){
            log.error("表名过长");
            return;
        }
        Date newDate = new Date();
        try {
            Date start = VerifyUtil.sdf.parse(startDate);
            // 根据 Calendar 完成时间的增加
            Calendar c = Calendar.getInstance();
            c.setTime(start);
            c.add(Calendar.DAY_OF_MONTH, interval);
            Date end = c.getTime();
            String endDate = VerifyUtil.sdf.format(end);

            // 如果查询的起始时间比当前时间早，则还可以继续查询
            while (newDate.compareTo(start) > 0) {
                List<Map<String, Object>> masterList =  masterDBDao.queryByCrtDt(tableName, startDate, endDate);
                List<Map<String, Object>> slaveList = slaveDBDao.queryByCrtDt(tableName, startDate, endDate);

                VerifyUtil.verifyResult(masterList, slaveList, tableName, log);

                // 一次对比结果集后，startDate变为endDate，endDate再加 interval 天
                startDate = endDate;
                c.clear();
                c.setTime(VerifyUtil.sdf.parse(endDate));
                c.add(Calendar.DAY_OF_MONTH, interval);
                endDate = VerifyUtil.sdf.format(c.getTime());
                start = VerifyUtil.sdf.parse(startDate);
            }
            MDC.clear();
        } catch (IOException | ParseException e ){
            e.printStackTrace();
        }
    }

    @Override
    public void queryById(String tableName, int pageSize) {
        // slf4j，设置变量，在xml中使用这些定义日志的名称，可以选择将日志输出到哪。适用于多线程
        MDC.put("logName",tableName);
        Logger log = LoggerFactory.getLogger(tableName);

        // 如果表名过长，则直接停止查询，防止sql注入，这是因为表名是字符串拼接的，而不是占位符
        // 表名也不可以用占位符
        if (tableName.length() > 50){
            return;
        }
        // 初始值为1，代表第一次查询
        int page = 1;
        while (true){
            List<Map<String, Object>> masterList = masterDBDao.queryById(tableName, page, pageSize);
            List<Map<String, Object>> slaveList = slaveDBDao.queryById(tableName, page, pageSize);
            if (masterList.size() == 0 || slaveList.size() == 0) {
                MDC.clear();
                return;
            }
            try {
                VerifyUtil.verifyResult(masterList, slaveList, tableName, log);
            } catch (IOException e) {
                e.printStackTrace();
            }
            page++;
        }
    }
}
