package com.verify.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public interface MasterDBDao {

    /**
     * 所有的表都有一个CRT_DT字段，我们可以根据该字段进行查询
     * @param tableName 数据库表名
     * @param crtName 表的创建时间字段,由于有些表的字段名称不一致,这里手动传递
     * @param startDate 查询起始时间
     * @param endDate 查询终止时间
     * @return 根据 CRT_DT 查询并排序得到的结果集
     */
    List<Map<String, Object>> queryByCrtDt(String tableName, String crtName, String startDate, String endDate, JdbcTemplate masterTemplate);

    /**
     * 根据主键 id 进行分页查询，这是为了防止 crt_dt 查询过慢，或者一些表不存在crt_dt字段而存在的
     * @param tableName 数据库表名
     * @param page 查询次数-1 也就是第几页
     * @param pageSize 每页显示数量，也就是一次查多少个
     * @return 结果集
     */
    List<Map<String, Object>> queryById(String tableName, int page, int pageSize, JdbcTemplate masterTemplate);

}
