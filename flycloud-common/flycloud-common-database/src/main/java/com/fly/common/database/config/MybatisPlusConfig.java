package com.fly.common.database.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fly.common.database.interceptor.DataScopeInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Mybatis-Plus 配置
 *
 * todo 使用spring.factories文件实现多模块下读取不同包名模块的配置文件, 即实现第三方bean注入spring容器
 *
 * @author lxs
 * @date 2023/3/22
 */
@EnableTransactionManagement(proxyTargetClass = true)
@AutoConfigurationPackage
@MapperScan("${mybatis-plus.mapperPackage}")
public class MybatisPlusConfig {

    //  todo 扫描mapper接口包有两种:
    //       法1: 默认扫描(也就是加注解), 由于Springboot默认是在主类(@SpringBootApplication)所在的包名下扫描并注册bean, 即在mapper接口类加上注解@Mapper即可
    //       法2: 手动扫描, 在相关启动配置类(如MybatisPlusConfig)加上@MapperScan("${mybatis-plus.mapperPackage}" 即可
    //       本项目用的是 法2 (因为基本上整个项目的包名前缀都是一样的,除非要注入第三方的bean); 如有需要支持多包可在注解配置 或 提升扫包等级 (例如 com.**.**.mapper)
    //    注意: @MapperScan 和 @Mapper 两者不能同时使用

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
