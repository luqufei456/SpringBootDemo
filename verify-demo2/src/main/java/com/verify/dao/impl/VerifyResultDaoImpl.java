package com.verify.dao.impl;

import com.verify.dao.VerifyResultDao;
import com.verify.dto.VerifyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VerifyResultDaoImpl implements VerifyResultDao {

    private JdbcTemplate verifyJdbcTemplate;

    @Autowired
    public VerifyResultDaoImpl(@Qualifier("verifyJdbcTemplate") JdbcTemplate verifyJdbcTemplate) {
        this.verifyJdbcTemplate = verifyJdbcTemplate;
    }

    //插入数据的sql
    private static final String ADD_SQL = "INSERT INTO VERIFY_RESULT(ID, VERIFY_TABLE, START_DATE, END_DATE, TOTAL, UNEQUAL, DTO_START_DATE, DTO_END_DATE, CRT_DATE) values(SEQ_ALL_TABLES.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?) ";
    // 根据表名,时间段查询是否被校验过,若被校验过,则停止校验
    private static final String QUERY_SQL = "SELECT COUNT(1) from VERIFY_RESULT WHERE VERIFY_TABLE = ? AND DTO_START_DATE = ? AND DTO_END_DATE = ? ";


    @Override
    public void save(VerifyResult verifyResult) {
        verifyJdbcTemplate.update(ADD_SQL, verifyResult.getVerifyTable(), verifyResult.getStartDate(), verifyResult.getEndDate(),
                verifyResult.getTotal(), verifyResult.getUnequal(), verifyResult.getDtoStartDate(), verifyResult.getDtoEndDate(),
                verifyResult.getCrtDate());
    }

    @Override
    public Integer findByVerifyResult(VerifyResult verifyResult) {
        return verifyJdbcTemplate.queryForInt(QUERY_SQL, verifyResult.getVerifyTable(), verifyResult.getDtoStartDate(), verifyResult.getDtoEndDate());
    }
}
