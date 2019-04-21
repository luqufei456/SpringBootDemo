package com.qflu.verify.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DataSourceConfiguration1 {

    // 主数据源必须标明@Primary表示优先，否则会报错
    // 数据源
    @Bean(name = "dataSource1")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    // 事务管理器
    @Bean(name = "dataSourceTransactionManager1")
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dataSource1")DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // 负责创建SqlSession
    @Bean(name = "sqlSessionFactory1")
    @Primary
    public SqlSessionFactory sessionFactory(@Qualifier("dataSource1")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        return sessionFactoryBean.getObject();
    }

    // 负责执行crud
    @Bean("sqlSessionTemplate1")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
