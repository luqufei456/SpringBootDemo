package com.verify.dto;

/**
 * datasource配置,及总开关
 */
public class DataSourceDto {
    // 数据库迁移中，原数据库地址
    private String masterUrl;
    // 数据库驱动
    private String masterDriver;
    // 数据库用户名，加密版
    private String masterUsername;
    // 数据库密码，加密版
    private String masterPassword;

    // 数据库迁移中，迁移到的数据库地址
    private String slaveUrl;
    private String slaveDriver;
    private String slaveUsername;
    private String slavePassword;

    // 是否更改数据源配置，更改的话则关闭原数据源，然后开启新的数据源
    private Boolean update;

    // 总开关,若为false,则定时器直接return,不会获取数据template
    private Boolean onOff;

    public String getMasterUrl() {
        return masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }

    public String getMasterDriver() {
        return masterDriver;
    }

    public void setMasterDriver(String masterDriver) {
        this.masterDriver = masterDriver;
    }

    public String getMasterUsername() {
        return masterUsername;
    }

    public void setMasterUsername(String masterUsername) {
        this.masterUsername = masterUsername;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public String getSlaveUrl() {
        return slaveUrl;
    }

    public void setSlaveUrl(String slaveUrl) {
        this.slaveUrl = slaveUrl;
    }

    public String getSlaveDriver() {
        return slaveDriver;
    }

    public void setSlaveDriver(String slaveDriver) {
        this.slaveDriver = slaveDriver;
    }

    public String getSlaveUsername() {
        return slaveUsername;
    }

    public void setSlaveUsername(String slaveUsername) {
        this.slaveUsername = slaveUsername;
    }

    public String getSlavePassword() {
        return slavePassword;
    }

    public void setSlavePassword(String slavePassword) {
        this.slavePassword = slavePassword;
    }

    public Boolean getUpdate() {
        return this.update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Boolean getOnOff() {
        return onOff;
    }

    public void setOnOff(Boolean onOff) {
        this.onOff = onOff;
    }
}
