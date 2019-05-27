package com.verify.dto;

import java.sql.Timestamp;

/**
 * 迁移数据出现问题后的详情实例
 */
public class VerifyInfo {
    // 主键
    private Long id;

    // 表名
    private String verifyTable;

    // 起始时间段 yyyy-MM-dd HH:mm:ss
    private String startDate;

    // 终止时间段 yyyy-MM-dd HH:mm:ss
    private String endDate;

    // 原数据源实体json字符串
    private String masterEntity;

    // 迁移后数据源json字符串，若不存在则为空字符串
    private String slaveEntity;

    // 字段创建时间
    private Timestamp crtDate;

    public Timestamp getCrtDate() {
        return crtDate;
    }

    public void setCrtDate(Timestamp crtDate) {
        this.crtDate = crtDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVerifyTable() {
        return verifyTable;
    }

    public void setVerifyTable(String verifyTable) {
        this.verifyTable = verifyTable;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMasterEntity() {
        return masterEntity;
    }

    public void setMasterEntity(String masterEntity) {
        this.masterEntity = masterEntity;
    }

    public String getSlaveEntity() {
        return slaveEntity;
    }

    public void setSlaveEntity(String slaveEntity) {
        this.slaveEntity = slaveEntity;
    }
}
