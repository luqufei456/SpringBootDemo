package com.qflu.verify.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.qflu.verify.utils.EncryptPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // 注意，这里的master1 必须与 datasource 里定义的 master不一样。否则加载时，会给你覆盖掉，又变成未解密的账号密码了。
    @Value("${spring.datasource.master1.username}")
    private String masterUsername;

    @Value("${spring.datasource.master1.password}")
    private String masterPassword;

    private static EncryptPropertyPlaceholderConfigurer configurer = new EncryptPropertyPlaceholderConfigurer();

    @Bean(name = "masterDataSource")
    @Primary
    // 这里表明了 prefix 之后，spring Boot 会自动帮我们在 properties 中找到对应的url password username 等，帮我们注入到return的dataSource中
    @ConfigurationProperties(prefix="spring.datasource.master")
    public DataSource masterDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        String masterUsernameDecode = configurer.convertProperty(masterUsername);
        String masterPasswordDecode = configurer.convertProperty(masterPassword);
        System.out.println("master数据源 loading ... username="+ masterUsernameDecode +"  password=" + masterPasswordDecode);
        dataSource.setUsername(masterUsernameDecode);
        dataSource.setPassword(masterPasswordDecode);
        return dataSource;
    }

    @Bean(name = "masterJdbcTemplate")
    public JdbcTemplate masterJdbcTemplate(
            @Qualifier("masterDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Value("${spring.datasource.slave1.username}")
    private String slaveUsername;

    @Value("${spring.datasource.slave1.password}")
    private String slavePassword;

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix="spring.datasource.slave")
    public DataSource slaveDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        String slaveUsernameDecode = configurer.convertProperty(slaveUsername);
        String slavePasswordDecode = configurer.convertProperty(slavePassword);
        System.out.println("slave数据源 loading ... username="+ slaveUsernameDecode +"  password=" + slavePasswordDecode);
        dataSource.setUsername(slaveUsernameDecode);
        dataSource.setPassword(slavePasswordDecode);
        return dataSource;
    }

    @Bean(name = "slaveJdbcTemplate")
    public JdbcTemplate slaveJdbcTemplate(
            @Qualifier("slaveDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
