package com.verify.service;

import com.verify.dto.TableDto;
import org.springframework.jdbc.core.JdbcTemplate;

public interface VerifyDBService {

    /**
     * 将时间间隔与startDate结合转为endDate，对查询结果进行一系列对比，生成日志等
     * @param tableDto 表配置字段
     * @param masterTemplate　原数据源template
     * @param slaveTemplate　迁移后数据源template
     * @return 返回成功或者失败
     */
    String queryByCrtDt(TableDto tableDto, JdbcTemplate masterTemplate, JdbcTemplate slaveTemplate);


    /**
     * 通过主键id进行分页查询、排序，一方面是为了速度，另一方面是防止有的表没有crt_dt字段
     * @param tableName 数据库表名
     * @param page 页码 表明从哪里开始查询,从1开始 结果范围为 (page-1)*pageSize -- page*pageSize
     * @param pageSize 每次查询的结果集个数
     * @param masterTemplate　原数据源template
     * @param slaveTemplate　迁移后数据源template
     * @return 返回成功或者失败
     */
    String queryById(String tableName, int page,int pageSize, JdbcTemplate masterTemplate, JdbcTemplate slaveTemplate);
}