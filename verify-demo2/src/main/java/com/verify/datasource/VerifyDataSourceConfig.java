package com.verify.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.verify.utils.EncryptPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 用于记录校验结果的datasource
 */
@Configuration
public class VerifyDataSourceConfig {
    private static EncryptPropertyPlaceholderConfigurer configurer = new EncryptPropertyPlaceholderConfigurer();

    // 注意，这里的master1 必须与 datasource 里定义的 master不一样。否则加载时，会给你覆盖掉，又变成未解密的账号密码了。
    // ConfigurationProperties 注解会让他自动注入set属性
    @Value("${spring.datasource.verify.url}")
    private String verifyUrl;

    @Value("${spring.datasource.verify.driver-class-name}")
    private String verifyDriver;

    @Value("${spring.datasource.verify.username}")
    private String verifyUsername;

    @Value("${spring.datasource.verify.password}")
    private String verifyPassword;

    @Bean(name = "verifyDataSource")
    @Primary
    // 这里去掉了之后,上面的命名就可以定义一样了,因为这个注解是boot的
    // 这里表明了 prefix 之后，spring Boot 会自动帮我们在 properties 中找到对应的url password username 等，帮我们注入到return的dataSource中
    // @ConfigurationProperties(prefix="spring.datasource.master")
    public DataSource verifyDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        String verifyUsernameDecode = configurer.convertProperty(verifyUsername);
        String verifyPasswordDecode = configurer.convertProperty(verifyPassword);
        System.out.println("校验结果数据源 loading ... username="+ verifyUsernameDecode +"  password=" + verifyPasswordDecode);
        dataSource.setUrl(verifyUrl);
        dataSource.setDriverClassName(verifyDriver);
        dataSource.setUsername(verifyUsernameDecode);
        dataSource.setPassword(verifyPasswordDecode);
        return dataSource;
    }

    @Bean(name = "verifyJdbcTemplate")
    public JdbcTemplate verifyJdbcTemplate(
            @Qualifier("verifyDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
