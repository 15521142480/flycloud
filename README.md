
### 系统前言:
>   flycloud-server 是一个微服务分布式架构系统, 主要做商城和音乐的一个业务管理系统.


### 目前集成了:
>   SpringCloudAlibaba + Nacos、
>   Mybatis-plus + Velocity、
>   SpringDoc + Swagger3.0、


### 框架目录结构:
```
flycloud
├─doc      -- 系统文档
├─flycloud-api         -- 内网接口
│  ├─flycloud-api-auth -- 授权对内接口
│  ├─flycloud_mall     -- 商城对内接口
│  ├─flycloud_system   -- 系统对内接口
├─flycloud-auth        -- 授权校验模块
├─flycloud-common      -- 公共模块
│  ├─flycloud-common-core       -- 公共模块核心（公共中的公共代码）
│  ├─flycloud-common-database   -- 数据库连接相关公共代码
│  └─flycloud-common-doc        -- springdoc(swagger)相关公共代码
│  └─flycloud-common-redis      -- redis相关公共代码
│  └─flycloud-common-seata      -- seata(分布式事务)相关公共代码
│  └─flycloud-common-security   -- 安全相关公共代码
│  ├─flycloud-common-rocketmq   -- rocketmq相关公共代码
├─flycloud-gateway   -- 网关服务
├─flycloud-generator -- 自动生成代码服务
└─flycloud-mall      -- 商城服务
└─flycloud-music     -- 音乐服务
└─flycloud-system    -- 系统服务
└─flycloud-test      -- 测试服务 (测试各种服务代码或中间件)
└─flycloud-user      -- 用户服务 
└─logs     -- 系统日志 
└─sql      -- 系统sql
```


### 框架服务:

| 服务                            | 地址                    |
|-------------------------------|-----------------------|
| flycloud-auth        授权校验服务   | http://127.0.0.1:8088 |
| flycloud-gateway     网关服务     | http://127.0.0.1:8080 |
| flycloud-generator   自动生成代码服务 | http://127.0.0.1:8089 |
| flycloud-mall        商城服务     | http://127.0.0.1:8083 |
| flycloud-music       音乐服务     | http://127.0.0.1:8084 |
| flycloud-system      系统服务     | http://127.0.0.1:8085 |
| flycloud-test        测试服务     | http://127.0.0.1:8099 |
| flycloud-user        用户服务     | http://127.0.0.1:8082 |


### generator自动生成代码说明:
>   有两种生成方式两种:
>   第一种: 通过后台管理生产
>   第二种: 直接访问接口生成: http://ip:网关端口/flycloud-generator/gen/generatorCode?tables=sys_user  (多个用,隔开; 默认生成的文件在当前根目录下, 具体看生成时的控制台日志信息)






### 实体模型(domain)说明:
>   BO -> 由于此系统采用的分布式微服务架构, 也就每个服务相对独立, 且都是服务之间的调用(网关), 所以DTO的概念换成了BO
>   VO -> 客户端(页面)展示的数据, 通常以json存在的形式
