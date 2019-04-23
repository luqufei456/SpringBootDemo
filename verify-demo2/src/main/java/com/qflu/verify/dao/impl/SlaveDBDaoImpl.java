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
        // 如果检测到表名长度超过了实际表名长度，则有可能是sql注入，直接返回一个空的结果集
        if (tableName.length() > 50) {
            List<Map<String, Object>> errorList = new ArrayList<>();
            errorList.add(new HashMap<String, Object>());
            return errorList;
        }
        String sql = "SELECT * FROM " + tableName
                + " WHERE CRT_DT > to_date(?, 'yyyy-MM-dd hh24:mi:ss') AND CRT_DT < to_date(?, 'yyyy-MM-dd hh24:mi:ss') ORDER BY CRT_DT ASC";
        return slaveJdbcTemplate.queryForList(sql, tableName, startDate, endDate);
    }
}
