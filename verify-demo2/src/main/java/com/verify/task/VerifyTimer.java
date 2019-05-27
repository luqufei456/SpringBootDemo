package com.verify.task;

import com.verify.consts.ConfigConst;
import com.verify.dao.VerifyResultDao;
import com.verify.datasource.DataSourceConfig;
import com.verify.dto.DataSourceDto;
import com.verify.dto.TableDto;
import com.verify.dto.VerifyResult;
import com.verify.service.VerifyDBService;
import com.verify.utils.LoggerUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VerifyTimer {

    private static Logger log = LoggerUtil.getLoggerByName("verify");

    @Autowired
    private VerifyDBService verifyDBService;

    @Autowired
    private VerifyResultDao verifyResultDao;

    /*@Override
    public void afterPropertiesSet() throws Exception {
        verifyDBService.queryByCrtDt(ConfigConst.tableName, ConfigConst.startDate, ConfigConst.interval, ConfigConst.crtName, ConfigConst.onOff);
    }*/

    // 统一管理template
    private static JdbcTemplate masterTemplate = null;
    private static JdbcTemplate slaveTemplate = null;

    private static void initTemplate() {
        DataSourceDto dataSourceDto = ConfigConst.dataSourceDto;
        if (dataSourceDto != null && dataSourceDto.getOnOff() != null && dataSourceDto.getUpdate() != null && dataSourceDto.getMasterDriver() != null
                && dataSourceDto.getMasterPassword() != null && dataSourceDto.getMasterUrl() != null && dataSourceDto.getMasterUsername() != null
                && dataSourceDto.getSlaveDriver() != null && dataSourceDto.getSlavePassword() != null && dataSourceDto.getSlaveUrl() != null && dataSourceDto.getSlaveUsername() != null) {

            if (!dataSourceDto.getOnOff()) {
                log.info("开关状态为false,不进行校验");
                return;
            }
            try {
                masterTemplate = DataSourceConfig.getJdbcTemplate("master", dataSourceDto);
                slaveTemplate = DataSourceConfig.getJdbcTemplate("slave", dataSourceDto);
            } catch (Exception e){
                log.error("数据源获取异常", e);
            }
        }else {
            log.error("数据源配置未完成,请检查配置");
        }
    }


    @Scheduled(fixedDelay = 10*60*1000)
    public void countScheduled(){
        initTemplate();
        if (masterTemplate == null || slaveTemplate == null){
            log.error("template为null,无法进行校验,请检查配置");
            return;
        }
        executeService(ConfigConst.table1);
        executeService(ConfigConst.table2);
        executeService(ConfigConst.table3);
        executeService(ConfigConst.table4);
        executeService(ConfigConst.table5);
    }

    private void executeService(TableDto tableDto) {
        //initTemplate();
        if (masterTemplate == null || slaveTemplate == null){
            log.error("template为null,无法进行校验,请检查配置");
            return;
        }

        if (tableDto != null && tableDto.getTableName() != null && tableDto.getCrtName() != null && tableDto.getFinalEndDate() != null
                && tableDto.getInterval() != null && tableDto.getStartDate() != null && tableDto.getOnOff() != null) {

            VerifyResult verifyResult = new VerifyResult();
            verifyResult.setVerifyTable(tableDto.getTableName());
            verifyResult.setDtoStartDate(tableDto.getStartDate());
            verifyResult.setDtoEndDate(tableDto.getFinalEndDate());
            // 校验是否已经校验过这个时间段
            if (verifyResultDao.findByVerifyResult(verifyResult) != 0) {
                log.info("表 " + tableDto.getTableName() + " 在 " + tableDto.getStartDate() + "--" + tableDto.getFinalEndDate()
                        + " 时间段已经校验过，现停止本次定时器执行");
                return;
            }

            verifyDBService.queryByCrtDt(tableDto, masterTemplate, slaveTemplate);
        }
        else {
            log.error("表数据配置未完全,请检查配置参数");
        }
    }



    //@Scheduled(fixedDelay = 2*60*60*1000)
    private void table1() {
        executeService(ConfigConst.table1);
    }

    //@Scheduled(fixedDelay = 2*60*60*1000)
    private void table2() {
        executeService(ConfigConst.table2);
    }

    //@Scheduled(fixedDelay = 2*60*60*1000)
    private void table3() {
        executeService(ConfigConst.table3);
    }

    //@Scheduled(fixedDelay = 2*60*60*1000)
    private void table4() {
        executeService(ConfigConst.table4);
    }

    //@Scheduled(fixedDelay = 2*60*60*1000)
    private void table5() {
        executeService(ConfigConst.table5);
    }



}
