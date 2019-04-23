package com.qflu.verify.service;

public interface VerifyDBService {

    /**
     * 将时间间隔与startDate结合转为endDate，对查询结果进行一系列对比，生成日志等
     * @param tableName 数据库表名
     * @param startDate 起始时间
     * @param interval 时间间隔，单位天
     */
    void queryByCrtDt(String tableName, String startDate, Integer interval);
}
