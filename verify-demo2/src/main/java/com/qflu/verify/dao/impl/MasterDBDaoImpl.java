package com.qflu.verify.dao.impl;

import com.qflu.verify.dao.MasterDBDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MasterDBDaoImpl implements MasterDBDao {

    private JdbcTemplate masterJdbcTemplate;

    @Autowired
    public MasterDBDaoImpl(@Qualifier("masterJdbcTemplate") JdbcTemplate masterJdbcTemplate) {
        this.masterJdbcTemplate = masterJdbcTemplate;
    }

    // JdbcTemplate对参数化sql查询进行了验证，从而防范了sql注入。
    // 注意oracle中to_date的格式为yyyy-MM-dd hh24:mi:ss，不能为yyyy-MM-dd HH:mm:ss
    @Override
    public List<Map<String, Object>> queryByCrtDt(String tableName, String startDate, String endDate) {
        String sql = "SELECT * FROM " + tableName
                + " WHERE CRT_DT > to_date(?, 'yyyy-MM-dd hh24:mi:ss') AND CRT_DT < to_date(?, 'yyyy-MM-dd hh24:mi:ss') ORDER BY CRT_DT ASC";
        return masterJdbcTemplate.queryForList(sql, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> queryById(String tableName, int page, int pageSize) {
        String sql = "SELECT * FROM ( SELECT a.*, ROWNUM ROWNO FROM ( SELECT * FROM " + tableName
                + " ORDER BY ID ASC ) a WHERE ROWNUM < ? ) b WHERE b.ROWNO > ? ";
        return masterJdbcTemplate.queryForList(sql, page*pageSize+1, (page-1)*pageSize);
    }
}
