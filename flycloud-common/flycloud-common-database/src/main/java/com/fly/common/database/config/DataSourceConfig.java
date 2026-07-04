package com.fly.common.database.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 数据源配置
 *
 * @author: lxs
 * @date: 2025/8/19
 */
@AutoConfiguration
public class DataSourceConfig {


    /**
     * 主数据源
     */
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.master")
//    public DataSource masterDataSource() {
//
//        return new DataSourceBuilder.create().build();
//    }


    /**
     * 从数据源
     */
//    @Bean(name = "slaveDatasource")
//    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.slave")
//    public DataSource slaveDataSource() {
//
//        return new DataSourceBuilder.create().build();
//    }

}
