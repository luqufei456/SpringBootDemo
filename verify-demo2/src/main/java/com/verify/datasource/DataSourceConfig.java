package com.verify.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.verify.consts.ConfigConst;
import com.verify.dto.DataSourceDto;
import com.verify.utils.EncryptPropertyPlaceholderConfigurer;
import com.verify.utils.LoggerUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

//@Configuration
public class DataSourceConfig {

    private static EncryptPropertyPlaceholderConfigurer configurer = new EncryptPropertyPlaceholderConfigurer();

/*
    // 注意，这里的master1 必须与 datasource 里定义的 master不一样。否则加载时，会给你覆盖掉，又变成未解密的账号密码了。
    // ConfigurationProperties 注解会让他自动注入set属性
    @Value("${spring.datasource.master.url}")
    private String masterUrl;

    @Value("${spring.datasource.master.driver-class-name}")
    private String masterDriver;

    @Value("${spring.datasource.master.username}")
    private String masterUsername;

    @Value("${spring.datasource.master.password}")
    private String masterPassword;

    @Bean(name = "masterDataSource")
    @Primary
    // 这里去掉了之后,上面的命名就可以定义一样了,因为这个注解是boot的
    // 这里表明了 prefix 之后，spring Boot 会自动帮我们在 properties 中找到对应的url password username 等，帮我们注入到return的dataSource中
    // @ConfigurationProperties(prefix="spring.datasource.master")
    public DataSource masterDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        String masterUsernameDecode = configurer.convertProperty(masterUsername);
        String masterPasswordDecode = configurer.convertProperty(masterPassword);
        System.out.println("master数据源 loading ... username="+ masterUsernameDecode +"  password=" + masterPasswordDecode);
        dataSource.setUrl(masterUrl);
        dataSource.setDriverClassName(masterDriver);
        dataSource.setUsername(masterUsernameDecode);
        dataSource.setPassword(masterPasswordDecode);
        return dataSource;
    }

    @Bean(name = "masterJdbcTemplate")
    public JdbcTemplate masterJdbcTemplate(
            @Qualifier("masterDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Value("${spring.datasource.slave.url}")
    private String slaveUrl;

    @Value("${spring.datasource.slave.driver-class-name}")
    private String slaveDriver;

    @Value("${spring.datasource.slave.username}")
    private String slaveUsername;

    @Value("${spring.datasource.slave.password}")
    private String slavePassword;

    @Bean(name = "slaveDataSource")
    // @ConfigurationProperties(prefix="spring.datasource.slave")
    public DataSource slaveDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        String slaveUsernameDecode = configurer.convertProperty(slaveUsername);
        String slavePasswordDecode = configurer.convertProperty(slavePassword);
        System.out.println("slave数据源 loading ... username="+ slaveUsernameDecode +"  password=" + slavePasswordDecode);
        dataSource.setUrl(slaveUrl);
        dataSource.setDriverClassName(slaveDriver);
        dataSource.setUsername(slaveUsernameDecode);
        dataSource.setPassword(slavePasswordDecode);
        return dataSource;
    }

    @Bean(name = "slaveJdbcTemplate")
    public JdbcTemplate slaveJdbcTemplate(
            @Qualifier("slaveDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    */

    private static DruidDataSource masterDataSource = null;
    private static JdbcTemplate masterTemplate = null;

    private static DruidDataSource slaveDataSource = null;
    private static JdbcTemplate slaveTemplate = null;

    private static Logger log = LoggerUtil.getLoggerByName("verify");

    public static JdbcTemplate getJdbcTemplate(String type, DataSourceDto dataSourceDto) {
        if ("master".equals(type)) {
            // 如果template不为null，并且不需要修改数据源,说明不需要更改数据源，直接返回
            if (masterTemplate != null && !dataSourceDto.getUpdate()) {
                return masterTemplate;
            }
            // 要么是程序刚运行，要么是需要修改数据源
            // 如果不为null，则为需要修改数据源的情况，先关闭连接池，再释放
            if (masterDataSource != null) {
                log.info("关闭master原数据源,切换新数据源");
                masterDataSource.close();
                masterDataSource = null;
                masterTemplate = null;
            }
            // 不管是更改数据源还是一开始的新增数据源，通用的
            masterDataSource = new DruidDataSource();
            String masterUsernameDecode = configurer.convertProperty(dataSourceDto.getMasterUsername());
            String masterPasswordDecode = configurer.convertProperty(dataSourceDto.getMasterPassword());
            log.info("master数据源 loading ... username="+ masterUsernameDecode +"  password=" + masterPasswordDecode);
            masterDataSource.setUrl(dataSourceDto.getMasterUrl());
            masterDataSource.setDriverClassName(dataSourceDto.getMasterDriver());
            masterDataSource.setUsername(masterUsernameDecode);
            masterDataSource.setPassword(masterPasswordDecode);

            // 保存template，然后返回
            masterTemplate = new JdbcTemplate(masterDataSource);
            return masterTemplate;
        }
        else if ("slave".equals(type)) {
            if (slaveTemplate != null && !dataSourceDto.getUpdate()) {
                return slaveTemplate;
            }

            if (slaveDataSource != null) {
                log.info("关闭slave原数据源,切换新数据源");
                slaveDataSource.close();
                slaveDataSource = null;
                slaveTemplate = null;
            }

            slaveDataSource = new DruidDataSource();
            String slaveUsernameDecode = configurer.convertProperty(dataSourceDto.getSlaveUsername());
            String slavePasswordDecode = configurer.convertProperty(dataSourceDto.getSlavePassword());
            log.info("slave数据源 loading ... username="+ slaveUsernameDecode +"  password=" + slavePasswordDecode);
            slaveDataSource.setUrl(dataSourceDto.getSlaveUrl());
            slaveDataSource.setDriverClassName(dataSourceDto.getSlaveDriver());
            slaveDataSource.setUsername(slaveUsernameDecode);
            slaveDataSource.setPassword(slavePasswordDecode);

            slaveTemplate = new JdbcTemplate(slaveDataSource);
            return slaveTemplate;
        }
        else {
            return null;
        }
    }

}