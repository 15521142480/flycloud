package com.fly.common.seata.config;

import com.fly.common.factory.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.PropertySource;

/**
 * Seata 客户端基础配置。
 *
 * <p>业务服务只需引入 {@code flycloud-common-seata}，本配置便会加载
 * {@code flycloud-seata.yaml}。Seata Starter 负责初始化 TM、RM 和
 * {@code @GlobalTransactional}；动态数据源负责为每个业务数据源创建 Seata 代理，
 * 因此这里不重复注册数据源代理。</p>
 *
 * <p>{@code undo_log} 属于业务库的受管表，必须随数据库迁移在发布前创建，禁止在应用
 * 启动期间执行 DDL。对应 MySQL 8 脚本见
 * {@code db/mysql/表结构/seata-client-undo-log-mysql8.sql}。</p>
 */
@AutoConfiguration
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:flycloud-seata.yaml")
public class SeataConfiguration {

}
