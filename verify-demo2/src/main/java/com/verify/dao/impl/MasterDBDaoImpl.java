package com.verify.dao.impl;

import com.verify.dao.MasterDBDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MasterDBDaoImpl implements MasterDBDao {
/*

    private JdbcTemplate masterJdbcTemplate;

    @Autowired
    public MasterDBDaoImpl(@Qualifier("masterJdbcTemplate") JdbcTemplate masterJdbcTemplate) {
        this.masterJdbcTemplate = masterJdbcTemplate;
    }
*/


    // JdbcTemplate对参数化sql查询进行了验证，从而防范了sql注入。
    // 注意oracle中to_date的格式为yyyy-MM-dd hh24:mi:ss，不能为yyyy-MM-dd HH:mm:ss
    @Override
    public List<Map<String, Object>> queryByCrtDt(String tableName, String crtName, String startDate, String endDate, JdbcTemplate masterTemplate) {
        String sql = "SELECT * FROM " + tableName
                + " WHERE " + crtName + " > to_date( ? , 'yyyy-MM-dd hh24:mi:ss') " +
                "AND " + crtName + " < to_date( ? , 'yyyy-MM-dd hh24:mi:ss') ";
        return masterTemplate.queryForList(sql, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> queryById(String tableName, int page, int pageSize, JdbcTemplate masterTemplate) {
        String sql = "SELECT a.* FROM ( SELECT t.*, ROWNUM ROWNO FROM " + tableName + " t WHERE ROWNUM < ? ) a WHERE a.ROWNO > ? ";
        return masterTemplate.queryForList(sql, page*pageSize+1, (page-1)*pageSize);
    }
}
