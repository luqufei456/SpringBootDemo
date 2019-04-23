package com.qflu.verify.service;

public interface VerifyDBService {

    /**
     * 将时间间隔与startDate结合转为endDate，对查询结果进行一系列对比，生成日志等
     * @param tableName 数据库表名
     * @param startDate 起始时间
     * @param interval 时间间隔，单位天，通过此间隔获取 endDate
     */
    void queryByCrtDt(String tableName, String startDate, Integer interval);

    /**
     * 通过主键id进行分页查询、排序，一方面是为了速度，另一方面是防止有的表没有crt_dt字段
     * @param tableName 数据库表名
     * @param pageSize 每次查询的结果集个数
     */
    void queryById(String tableName, int pageSize);
}
