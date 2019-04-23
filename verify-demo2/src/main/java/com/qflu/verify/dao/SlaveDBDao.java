package com.qflu.verify.dao;

import java.util.List;
import java.util.Map;

public interface SlaveDBDao {

    /**
     * 所有的表都有一个CRT_DT字段，我们可以根据该字段进行查询
     * @param tableName 数据库表名
     * @param startDate 查询起始时间
     * @param endDate 查询终止时间
     * @return 根据 CRT_DT 查询并排序得到的结果集
     */
    List<Map<String, Object>> queryByCrtDt(String tableName, String startDate, String endDate);

    /**
     * 根据主键 id 进行分页查询，这是为了防止 crt_dt 查询过慢，或者一些表不存在crt_dt字段而存在的
     * @param tableName 数据库表名
     * @param page 查询次数-1 也就是第几页
     * @param pageSize 每页显示数量，也就是一次查多少个
     * @return 结果集
     */
    List<Map<String, Object>> queryById(String tableName, int page, int pageSize);
}
