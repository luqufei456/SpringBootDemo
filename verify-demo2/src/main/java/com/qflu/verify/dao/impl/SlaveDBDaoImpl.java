package com.qflu.verify.dao.impl;

import com.qflu.verify.dao.SlaveDBDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SlaveDBDaoImpl implements SlaveDBDao {

    private JdbcTemplate slaveJdbcTemplate;

    @Autowired
    public SlaveDBDaoImpl(@Qualifier("slaveJdbcTemplate") JdbcTemplate slaveJdbcTemplate) {
        this.slaveJdbcTemplate = slaveJdbcTemplate;
    }

    // JdbcTemplate对参数化sql查询进行了验证，从而防范了sql注入。
    @Override
    public List<Map<String, Object>> queryByCrtDt(String tableName, String startDate, String endDate) {
        String sql = "SELECT * FROM " + tableName
                + " WHERE CRT_DT > to_date(?, 'yyyy-MM-dd hh24:mi:ss') AND CRT_DT < to_date(?, 'yyyy-MM-dd hh24:mi:ss') ORDER BY CRT_DT ASC";
        return slaveJdbcTemplate.queryForList(sql, tableName, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> queryById(String tableName, int page, int pageSize) {
        String sql = "SELECT * FROM ( SELECT a.*, ROWNUM ROWNO FROM ( SELECT * FROM " + tableName
                + " ORDER BY ID ASC ) a WHERE ROWNUM < ? ) b WHERE b.ROWNO > ? ";
        return slaveJdbcTemplate.queryForList(sql, page*pageSize+1, (page-1)*pageSize);
    }
}
