package com.verify.dao.impl;

import com.verify.dao.VerifyInfoDao;
import com.verify.dto.VerifyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VerifyInfoDaoImpl implements VerifyInfoDao {

    private JdbcTemplate verifyJdbcTemplate;

    @Autowired
    public VerifyInfoDaoImpl(@Qualifier("verifyJdbcTemplate") JdbcTemplate verifyJdbcTemplate) {
        this.verifyJdbcTemplate = verifyJdbcTemplate;
    }

    //插入数据的sql
    private static final String ADD_SQL = "INSERT INTO VERIFY_INFO(ID, VERIFY_TABLE, START_DATE, END_DATE, MASTER_ENTITY, SLAVE_ENTITY, CRT_DATE) values(SEQ_ALL_TABLES.NEXTVAL, ?, ?, ?, ?, ?, ?) ";

    @Override
    public void save(VerifyInfo verifyInfo) {
        verifyJdbcTemplate.update(ADD_SQL, verifyInfo.getVerifyTable(), verifyInfo.getStartDate(), verifyInfo.getEndDate(),
                verifyInfo.getMasterEntity(), verifyInfo.getSlaveEntity(), verifyInfo.getCrtDate());
    }
}
