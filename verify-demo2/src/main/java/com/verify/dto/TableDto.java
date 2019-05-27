package com.verify.dto;

/**
 * 表dto，其中存储了表名，查询起始时间，查询中止时间，查询时间间隔，crtName，开关等
 */
public class TableDto {

    // 表名,要求不为空,长度小于50
    private String tableName;
    // 起始时间，字符串，格式为 "2018-04-05"
    private String startDate;
    // 最后的终止时间，要和单次循环的endDate区分开，字符串，格式为 "2018-04-05"
    private String finalEndDate;
    // 按CRT_DT查询时，间隔时间，单位为小时，最高为48小时
    private Integer interval;
    // 有些表的创建时间字段为 CRT_DT 有些为 CRT_DATE,这里让查询时,他们自己传
    private String crtName;
    // 定义查询开关是否开启,未开启则直接返回,不进行查询
    private Boolean onOff;

    // page，页码，从1开始 起始位置为 page*pageSize
    private Integer page = 1;
    // 每次查询的数量
    private Integer pageSize = 1000;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinalEndDate() {
        return finalEndDate;
    }

    public void setFinalEndDate(String finalEndDate) {
        this.finalEndDate = finalEndDate;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getCrtName() {
        return crtName;
    }

    public void setCrtName(String crtName) {
        this.crtName = crtName;
    }

    public Boolean getOnOff() {
        return onOff;
    }

    public void setOnOff(Boolean onOff) {
        this.onOff = onOff;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
