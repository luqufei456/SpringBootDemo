package com.verify.dto;

import java.sql.Timestamp;

/**
 * 每个时间段校验结果实例
 */
public class VerifyResult {
    // 主键
    private Long id;

    // 表名
    private String verifyTable;

    // 配置中心中配置的起始时间与终止时间，如果有这两个字段代表已经校验过
    private String dtoStartDate;

    private String dtoEndDate;

    private Timestamp crtDate;

    // 起始时间段 yyyy-MM-dd HH:mm:ss
    private String startDate;

    // 终止时间段 yyyy-MM-dd HH:mm:ss
    private String endDate;

    // 对比总条数
    private Integer total;

    // 不同的条数
    private Integer unequal;

    public String getDtoStartDate() {
        return dtoStartDate;
    }

    public void setDtoStartDate(String dtoStartDate) {
        this.dtoStartDate = dtoStartDate;
    }

    public String getDtoEndDate() {
        return dtoEndDate;
    }

    public void setDtoEndDate(String dtoEndDate) {
        this.dtoEndDate = dtoEndDate;
    }

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getUnequal() {
        return unequal;
    }

    public void setUnequal(Integer unequal) {
        this.unequal = unequal;
    }
}
