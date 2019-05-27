package com.verify.service.impl;

import com.verify.dao.MasterDBDao;
import com.verify.dao.SlaveDBDao;
import com.verify.dao.VerifyResultDao;
import com.verify.dto.DataSourceDto;
import com.verify.dto.TableDto;
import com.verify.dto.VerifyInfo;
import com.verify.dto.VerifyResult;
import com.verify.service.VerifyDBService;
import com.verify.utils.LoggerUtil;
import com.verify.utils.VerifyUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@Service
public class VerifyDBServiceImpl implements VerifyDBService {

    private MasterDBDao masterDBDao;
    private SlaveDBDao slaveDBDao;

    private VerifyResultDao verifyResultDao;

    private VerifyUtil verifyUtil;

    private Logger log = LoggerUtil.getLoggerByName("verify");

    public static Map<String, Integer> unequals = new HashMap<String, Integer>();

    @Autowired
    public VerifyDBServiceImpl(MasterDBDao masterDBDao, SlaveDBDao slaveDBDao, VerifyResultDao verifyResultDao, VerifyUtil verifyUtil) {
        this.masterDBDao = masterDBDao;
        this.slaveDBDao = slaveDBDao;
        this.verifyResultDao = verifyResultDao;
        this.verifyUtil = verifyUtil;
    }

    @Override
    public String queryByCrtDt(TableDto tableDto, JdbcTemplate masterTemplate, JdbcTemplate slaveTemplate) {

        if (!tableDto.getOnOff()) {
            log.error("表 [" + tableDto.getTableName() + "] 开关并未打开");
            return "table switch off";
        }
        // 如果表名过长，则直接停止查询，防止sql注入，这是因为表名是字符串拼接的，而不是占位符
        // 表名也不可以用占位符
        // 参数校验
        if (tableDto.getTableName().length() > 50 || StringUtils.isEmpty(tableDto.getTableName())){
            log.error("表名不符合规范");
            return "The table name does not conform to the specification";
        }
        String startDate = tableDto.getStartDate() + " 00:00:00";
        String finalEndDate = tableDto.getFinalEndDate() + " 00:00:00";
        try {
            Date start = VerifyUtil.sdf.parse(startDate);
            if (start.compareTo(new Date()) > 0) {
                log.error("startDate 必须位于当前时间之前");
                return "startDate must be before the newDate";
            }
        } catch (ParseException e) {
            log.error("startDate 或 finalEndDate 格式错误，要求为 [yyyy-MM-dd]");
            return "startDate or finalEndDate formal error , [yyyy-MM-dd]";
        }
        if (tableDto.getInterval() <= 0 || tableDto.getInterval() > 48) {
            log.error("时间间隔必须大于0，小于等于48");
            return "Time interval must between 1 and 48";
        }

        try {
            Date start = VerifyUtil.sdf.parse(startDate);
            Date finalEnd = VerifyUtil.sdf.parse(finalEndDate);
            // 根据 Calendar 完成时间的增加
            Calendar c = Calendar.getInstance();
            c.setTime(start);
            // 这里HOUR HOUR_OF_DAY是一样的，get时用这种获取的显示才有区别
            c.add(Calendar.HOUR_OF_DAY, tableDto.getInterval());
            Date end = c.getTime();
            String endDate = VerifyUtil.sdf.format(end);

            //String logName = tableDto.getTableName() + tableDto.getStartDate() + "-" + tableDto.getFinalEndDate();
            //Logger tableLog = LoggerUtil.getLoggerByName(logName);

            // 如果查询的起始时间比终止时间早，则还可以继续查询
            String unequalKey = "";
            while (finalEnd.compareTo(start) > 0) {
                // 初始化时间段异常数据数量，key 为表名+时间段
                unequalKey = tableDto.getTableName() + startDate + endDate;
                unequals.put(unequalKey, 0);
                List<Map<String, Object>> masterList;
                List<Map<String, Object>> slaveList;
                try {
                    masterList =  masterDBDao.queryByCrtDt(tableDto.getTableName(), tableDto.getCrtName(), startDate, endDate, masterTemplate);
                    slaveList = slaveDBDao.queryByCrtDt(tableDto.getTableName(), tableDto.getCrtName(), startDate, endDate, slaveTemplate);
                } catch (Exception e) {
                    log.error(tableDto.getTableName()+" 表查询出现错误", e);
                    return "The Network Adapter could not establish the connection";
                }


                // 记录本次对比条数
                int total = masterList.size();

                // 设置单次查询时间段内共享的verifyInfo数据
                VerifyInfo verifyInfo = new VerifyInfo();
                verifyInfo.setVerifyTable(tableDto.getTableName());
                verifyInfo.setStartDate(startDate);
                verifyInfo.setEndDate(endDate);

                // 校验
                verifyUtil.verifyResult(masterList, slaveList, verifyInfo);

                // 一次对比结果集后，startDate变为endDate，endDate再加 interval 天
                startDate = endDate;
                c.clear();
                c.setTime(VerifyUtil.sdf.parse(endDate));
                c.add(Calendar.HOUR_OF_DAY, tableDto.getInterval());
                endDate = VerifyUtil.sdf.format(c.getTime());
                start = VerifyUtil.sdf.parse(startDate);

                // 记录单次时间段校验数据结果
                VerifyResult verifyResult = new VerifyResult();
                verifyResult.setVerifyTable(tableDto.getTableName());
                verifyResult.setStartDate(startDate);
                verifyResult.setEndDate(endDate);
                verifyResult.setTotal(total);
                verifyResult.setDtoStartDate(tableDto.getStartDate());
                verifyResult.setDtoEndDate(tableDto.getFinalEndDate());
                verifyResult.setCrtDate(new Timestamp(new Date().getTime()));
                verifyResult.setUnequal(unequals.get(unequalKey));
                verifyResultDao.save(verifyResult);
                // 清理掉，防止次数累计后，占用内存
                unequals.remove(unequalKey);
            }
            // tableLog.info("本次校验完成，共校验 " + verifyTotal + " 条数据，结果如上");
        } catch (Exception e ){
            log.error(tableDto.getTableName()+" 系统异常或查询出错，请检查参数", e);
            return "error by system, please check the param";
        }
        log.info(tableDto.getTableName() + " 表本次校验时间区间为 " + tableDto.getStartDate() + "--" + tableDto.getFinalEndDate());
        return "success";
    }

    @Override
    public String queryById(String tableName, int page,int pageSize, JdbcTemplate masterTemplate, JdbcTemplate slaveTemplate) {
        Logger log = LoggerUtil.getLoggerByName("verify");

        // 如果表名过长，则直接停止查询，防止sql注入，这是因为表名是字符串拼接的，而不是占位符
        // 表名也不可以用占位符
        if (tableName.length() > 50 || StringUtils.isEmpty(tableName)){
            log.error("表名不符合规范");
            return "表名不符合规范";
        }
        if (pageSize >= 10000 || pageSize < 1000) {
            log.error("一次查询的数目请保证在1k-10k之间");
            return "一次查询的数目请保证在1k-10k之间";
        }

        String logName = tableName;
        Logger tableLog = LoggerUtil.getLoggerByName(logName);

        // 初始值为1，代表第一次查询
        while (true){
            List<Map<String, Object>> masterList = masterDBDao.queryById(tableName, page, pageSize, masterTemplate);
            List<Map<String, Object>> slaveList = slaveDBDao.queryById(tableName, page, pageSize, slaveTemplate);
            if (masterList.size() == 0 || slaveList.size() == 0) {
                return "success";
            }
            /*try {
                verifyUtil.verifyResult(masterList, slaveList, tableLog);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            page++;
        }
    }

}