package com.fly.common.database.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fly.common.database.interceptor.DataScopeInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Mybatis-Plus 配置
 *
 * @author lxs
 * @date 2023/3/22
 */
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
//@MapperScan("${mybatis-plus.mapperPackage}") // todo 注意: @MapperScan 和 @Mapper 两者不能同时使用
public class MybatisPlusConfig {

    // todo 扫描mapper接口包有两种: (法1: 自定义包配置然后MybatisPlusConfiguration启动配置文件加上@MapperScan("${mybatis-plus.mapperPackage}"), 法2: 在mapper接口类使用@Mapper注解即可 )

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 数据权限
        mybatisPlusInterceptor.addInnerInterceptor(new DataScopeInnerInterceptor());
        // 分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 乐观锁
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防止全表更新与删除
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        return mybatisPlusInterceptor;
    }


    /**
     * 自动填充字段配置
     */
//    @Bean
//    public FieldMetaObjectHandler fieldMetaObjectHandler(){
//        return new FieldMetaObjectHandler();
//    }

}
